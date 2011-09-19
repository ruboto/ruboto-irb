require 'ruboto/base'

####################################################################################
####################################################################################
##
## ruboto/broadcast_receiver.rb
##
####################################################################################
####################################################################################

#
# Legacy BroadcastReceiver Subclass Setup
#

module Ruboto
  module BroadcastReceiver
    def handle_receive(&block)
      instance_exec &block
      on_receive($context, nil)
    end
  end
end

#
# Basic BroadcastReceiver Setup
#

def ruboto_configure_broadcast_receiver(klass)
  klass.class_eval do
    include Ruboto::BroadcastReceiver
    
    def on_receive(context, intent)
    end
  end
end

ruboto_import "org.ruboto.RubotoBroadcastReceiver"
ruboto_configure_broadcast_receiver(RubotoBroadcastReceiver)

