# Write your code here :-)
import network
from machine import UART
from machine import Pin

_tx = machine.Pin(1)
_rx = machine.Pin(3)
def do_connect():

    wlan = network.WLAN(network.STA_IF)
    wlan.active(True)
    if not wlan.isconnected():
        print('connecting to network...')
        wlan.connect('essid', 'password')
        while not wlan.isconnected():
            pass
    print('network config:', wlan.ifconfig())

def init():
    do_connect()
    uart = UART(0, baudrate=115200, tx=_tx, rx=_rx)

init()
receive_message = ''
while True:
    if (uart.read(8) == '#'):
        while (uart.read(8) != 'n'):
            receive_message = receive_message+uart.read(8)
        sendToAda(receive_message)
        receive_message = ''
    sleep(0.01)
