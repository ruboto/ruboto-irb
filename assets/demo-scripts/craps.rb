#!/usr/bin/env ruby
######################################################
#
# craps.rb (by Daniel Jackoway)
#
# Simple version of craps, the casin game, in Ruboto
# script form
#
######################################################

#subclasses_of stolen from ActiveSupport
# File activesupport/lib/active_support/core_ext/object/extending.rb, line 29
def subclasses_of(*superclasses) #:nodoc:
  subclasses = []

  superclasses.each do |sup|
    ObjectSpace.each_object(Class) do |k|
      if superclasses.any? { |superclass| k < superclass } &&
          (k.name.blank? || eval("defined?(::#{k}) && ::#{k}.object_id == k.object_id"))
        subclasses << k
      end
    end
    subclasses.uniq!
  end
  subclasses
end
# subclasses_of needs blank?
# File activesupport/lib/active_support/core_ext/object/blank.rb, line 12
class Object
  def blank?
    respond_to?(:empty?) ? empty? : !self
  end
end


####################################################################
#
# Craps
# This section has the stuff driving the actual game.
# In other words, you'd use this section even if it were a command
# line game or a web-based game.
#
####################################################################

# Slightly overkill, but this easily gives me random dice rolls
# and lets me pass both the individual rolls but also a #sum method
class Roll
  attr_accessor :dice, :sum

  def initialize
    @dice = roll, roll
    @sum = dice[0] + dice[1]
  end

  def roll
    rand(6) + 1
  end
end

# abstract Bet class
# All Bets must define a play(roll, point) method and
# a name method
# play returns
#    nil if the bet stays
#    -1 if they lose
#     a number giving the payout as a factor of the bet
#     (0 means they get their money back, 1 means it pays 1:1 odds,
#     2 means it pays 2:1, etc)
class Bet
  attr_reader :money

  def initialize(money)
    @money = money
  end
end

#Define all the bets
class Pass < Bet
  def play(roll, point)
    if point
      if point == roll.sum
        return 1
      elsif roll.sum == 7
        return -1
      else
        return nil
      end
    else
      return -1 if [2,3,12].include?(roll.sum)
      return 1 if [7,11].include?(roll.sum)
      return nil
    end
  end

  def name
    "Pass Line"
  end
end

class Field < Bet
  def play(roll, point)
    return 1 if [3,4,9,10,11].include?(roll.sum)
    return 2 if [2,12].include?(roll.sum)
    -1
  end

  def name
    "Field"
  end
end

class SnakeEyes < Bet
  def play(roll, point)
    return 30 if roll.dice[0] == roll.dice[1] && !roll.dice.detect {|n| n != 1}
    -1
  end

  def name
    "Snake Eyes"
  end
end

class BoxCars < Bet
  def play(roll, point)
    return 30 if roll.dice[0] == roll.dice[1] && !roll.dice.detect {|n| n != 6}
    -1
  end

  def name
    "Snake Eyes"
  end
end

def bet(klass, amount)
  if amount > $money
    toast "you don't have that much money!"
    nil
  else
    toast "bet placed"
    $money -= amount
    klass.new(amount)
  end
end

# the player's money
$money = 10000
# the point. nil if we're on the come-out
$point = nil
# all of the bets on the table
$bets = []
# all of the types of bets one can place
AVAILABLE_BETS = subclasses_of Bet


###############################################################################
#
# Ruboto part
# This part is majorly concerned with integrating with Android APIs
# for display purposes, but it does do a little bit of driving the game along.
#
###############################################################################

require 'ruboto'
confirm_ruboto_version(3, false)

ruboto_import_widgets :TextView, :LinearLayout, :Button, :ListView, :EditText


$activity.start_ruboto_activity "$craps" do
  setTitle "Craps"
  setup_content do
    linear_layout :orientation => LinearLayout::VERTICAL do
      linear_layout :orientation => LinearLayout::HORIZONTAL do
        text_view :text => "You have $"
        @money_view = text_view :text => "#{$money}"
      end
      @point_view = text_view :text => "Come-out"
      linear_layout :orientation => LinearLayout::HORIZONTAL do
        button :text => "Roll", :width => :wrap_content
        @roll_view = text_view :text => ""
      end
      button :text => "Place a Bet", :width => :wrap_content
    end
  end

  handle_click do |view|
    case view.getText
    when "Roll" then roll
    when "Place a Bet" then launch_bets
    else toast "foo"
    end
  end
end

def launch_bets
  self.start_ruboto_activity("$bet_list") do
    setTitle "Place a Bet"
    setup_content {list_view :list => AVAILABLE_BETS}
    handle_item_click do |adapter_view, view, pos, item_id|
      klass = AVAILABLE_BETS[pos]
      $activity.start_ruboto_activity "$money_dialog" do
        setTitle klass.to_s
        setup_content do
          linear_layout :orientation => LinearLayout::VERTICAL do
            @bet_amount = edit_text
            button :text => "Place Bet"
          end
        end

        handle_click do |view|
          if view.getText == "Place Bet"
            amount = @bet_amount.getText.to_s.to_i
            if amount > 0
              $bets << bet(klass, amount)
            else
              toast "enter a valid integer greater than 0"
            end
          else
            toast "something bad happened"
          end
        end
      end
    end
  end
end



def roll
  $bets.compact!

  roll = Roll.new
  @roll_view.setText roll.dice.inspect

  delete = []
  $bets.each do |bet|
    result = bet.play(roll, $point)
    if !result
      toast "your #{bet.name} bet is still on"
    elsif result < 0
      toast "you lost your #{bet.name} bet"
      delete << bet
    else
      # they get their money back (1 * money) + the payout (result * money)
      toast "you won your #{bet.name} bet!"
      $money += bet.money * (1 + result)
      delete << bet
    end
  end
  delete.each {|bet| $bets.delete bet }


  if $point
    $point = nil if $point == roll.sum || roll.sum == 7
  else
    $point = roll.sum if [4,5,6,8,9,10].include? roll.sum
  end

  @money_view.setText $money.to_s
  @point_view.setText $point ? "The point is #{$point}" : "Come-out"
end
