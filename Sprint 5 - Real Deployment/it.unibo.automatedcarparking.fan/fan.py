#!/usr/bin/python
import RPi.GPIO as GPIO
import time
import argparse

GPIO.setmode(GPIO.BOARD)

parser = argparse.ArgumentParser(description='Control a fan.')
group = parser.add_mutually_exclusive_group()
group.add_argument('--setup', action='store_true', help='Setup GPIO PIN.')
group.add_argument('--start', action='store_true', help='Start fan.')
group.add_argument('--stop', action='store_true', help='Stop fan.')
parser.add_argument('--pin', type=int, default=15, help='Control pin number. Note: GPIO Board number.')

args = parser.parse_args()


def setup(pin: int):
    GPIO.setwarnings(False)
    GPIO.setup(pin, GPIO.OUT)


def start(pin : int):
    GPIO.output(pin, GPIO.HIGH)
    time.sleep(0.200)


def stop(pin : int):
    GPIO.output(pin, GPIO.LOW)
    time.sleep(0.200)


def main():
    setup(pin=args.pin)
    if args.start:
        start(pin=args.pin)
    elif args.stop:
        stop(pin=args.pin)


if __name__ == '__main__':
    main()


