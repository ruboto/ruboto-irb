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
# needs blank?
# File activesupport/lib/active_support/core_ext/object/blank.rb, line 12
class Object
  def blank?
    respond_to?(:empty?) ? empty? : !self
  end
end




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
    puts "you don't have that much money!"
    nil
  else
    $money -= amount
    klass.new(amount)
  end
end

# the player's money
$money = 10000
point = nil
$bets = []
AVAILABLE_BETS = subclasses_of Bet

while true
  # place bets
  $bets << bet(Pass, 50)  unless $bets.detect{|b| b.class == Pass}
  $bets << bet(Field, 25)
  $bets.compact!

  roll = Roll.new
  puts roll.dice.inspect

  delete = []
  $bets.each do |bet|
    result = bet.play(roll, point)
    if !result
      puts "your #{bet.name} bet is still on"
    elsif result < 0
      puts "you lost your #{bet.name} bet"
      delete << bet
    else
      # they get their money back (1 * money) + the payout (result * money)
      puts "you won your #{bet.name} bet!"
      $money += bet.money * (1 + result)
      delete << bet
    end
  end
  delete.each {|bet| $bets.delete bet }


  if point
    point = nil if point == roll.sum || roll.sum == 7
  else
    point = roll.sum if [4,5,6,8,9,10].include? roll.sum
  end
  puts point ? "the point is #{point}" : "still on the come-out"

  puts "money: #{$money}"
  puts ''
  sleep(1)
end
