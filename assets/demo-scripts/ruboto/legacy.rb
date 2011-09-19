require 'ruboto/activity'

####################################################################################
####################################################################################
##
## ruboto/legacy.rb
##
####################################################################################
####################################################################################

#
# Old handle methods
#

module Ruboto
  module Callbacks
    def method_missing(name, *args, &block)
      if name.to_s =~ /^handle_(.*)/ and (const = self.class.const_get("CB_#{$1.upcase}"))
        setCallbackProc(const, block)
        self
      else
        super
      end
    end

    def respond_to?(name)
      return true if name.to_s =~ /^handle_(.*)/ and self.class.const_get("CB_#{$1.upcase}")
      super
    end

    def initialize_handlers(&block)
      instance_eval &block
      self
    end
  end
end

#
# Allows RubotoActivity to handle callbacks covering Class based handlers
#

def ruboto_register_handler(handler_class, unique_name, for_class, method_name)
  klass_name = handler_class[/.+\.([A-Z].+)/, 1]
  klass      = ruboto_import handler_class

  unless RubotoActivity.method_defined? "#{unique_name}_handler"
    RubotoActivity.class_eval "
      def #{unique_name}_handler
        @#{unique_name}_handler ||= #{klass_name}.new
      end

      def handle_#{unique_name}(&block)
        #{unique_name}_handler.handle_#{unique_name} &block
        self
      end
    "
  end

  unless for_class.method_defined? "orig_#{method_name}"
    for_class.class_eval "
      alias_method :orig_#{method_name}, :#{method_name}
      def #{method_name}(handler)
        orig_#{method_name}(handler.kind_of?(RubotoActivity) ? handler.#{unique_name}_handler : handler)
      end
    "
  end
end

def setup_button
  Button.class_eval do
    def configure(context, params = {})
      setOnClickListener(context)
      super(context, params)
    end
  end

  ruboto_register_handler("org.ruboto.callbacks.RubotoOnClickListener", "click", Button, "setOnClickListener")
end

def setup_image_button
  ImageButton.class_eval do
    def configure(context, params = {})
      setOnClickListener(context)
      super(context, params)
    end
  end

  ruboto_register_handler("org.ruboto.callbacks.RubotoOnClickListener", "click", ImageButton, "setOnClickListener")
end

def setup_list_view
  Java::android.widget.ListView.class_eval do
    attr_reader :adapter, :adapter_list

    def configure(context, params = {})
      if params.has_key? :list
        @adapter_list = Java::java.util.ArrayList.new
        @adapter_list.addAll(params[:list])
        item_layout = params.delete(:item_layout) || R::layout::simple_list_item_1
        @adapter    = Java::android.widget.ArrayAdapter.new(context, item_layout, @adapter_list)
        setAdapter @adapter
        params.delete :list
      end
      if params.has_key? :adapter
        @adapter = params[:adapter]
      end
      setOnItemClickListener(context) # legacy
      super(context, params)
    end

    def reload_list(list)
      @adapter_list.clear
      @adapter_list.addAll(list)
      @adapter.notifyDataSetChanged
      invalidate
    end
  end

  ruboto_register_handler("org.ruboto.callbacks.RubotoOnItemClickListener", "item_click", Java::android.widget.ListView, "setOnItemClickListener") # legacy
end

def setup_spinner
  Java::android.widget.Spinner.class_eval do
    attr_reader :adapter, :adapter_list

    def configure(context, params = {})
      if params.has_key? :list
        @adapter_list = Java::java.util.ArrayList.new
        @adapter_list.addAll(params[:list])
        item_layout = params.delete(:item_layout) || R::layout::simple_spinner_item
        @adapter    = Java::android.widget.ArrayAdapter.new(context, item_layout, @adapter_list)
        @adapter.setDropDownViewResource(params.delete(:dropdown_layout) || R::layout::simple_spinner_dropdown_item)
        setAdapter @adapter
        params.delete :list
      end
      setOnItemSelectedListener(context) # legacy
      super(context, params)
    end

    def reload_list(list)
      @adapter.clear
      @adapter.addAll(list)
      @adapter.notifyDataSetChanged
      invalidate
    end
  end

  ruboto_register_handler("org.ruboto.callbacks.RubotoOnItemSelectedListener", "item_selected", Java::android.widget.Spinner, "setOnItemSelectedListener") # legacy
end

