# Write your code here :-)
from microbit import *
import radio
import microbit

_tx = microbit.pin0
_rx = microbit.pin1
def init():
    microbit.uart.init(baudrate=115200, bits=8, parity=None, stop=1, tx=_tx, rx=_rx)
    radio.config(channel=2)
    radio.on()
def ack_mess(num_sen):
    return '#a'+str(num_sen)+'*'
def decode_num_sen(message):
    mess = message.split('-')
    return mess[0]
init()
while True:
    message = radio.receive()
    if (message):
        radio.send(ack_mess(decode_num_sen(message)))
        microbit.uart.write(message)
    if (microbit.uart.any()):
        return_message = microbit.uart.readline()
        radio.send(return_message)
    sleep(10)
