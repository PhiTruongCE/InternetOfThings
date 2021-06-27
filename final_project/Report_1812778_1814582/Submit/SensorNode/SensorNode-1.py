from microbit import *
import radio
radio.config(channel=2)
radio.on()

receive_flag = False

def packet(temp, light):
    message = '#1-'+str(temp)+'-'+str(light)+'-*'
    return message

def decode_message(message):
    mess = message.split('-')
    return mess

send_count = 1000
wait_count = 500
while True:
    #send data every 10 second
    if (send_count == 1000):
        temp = int(temperature())
        light = int(display.read_light_level())
        mess = packet(temp, light)
        radio.send(mess)
        receive_flag = False

    #receive ack or control message
    control_message = radio.receive()
    if (control_message):
        header = decode_message(control_message)
        if (header[0] == '#a1*'):
            receive_flag = True
            wait_count = 500
        elif (header[0] == '#c1'):
            if (header[1] == '10'):
                display.show(Image.SAD)
            elif (header[1] == '11'):
                display.show(Image.HAPPY)
            elif (header[1] == '21'):
                display.show(Image.YES)
            else:
                display.show(Image.NO)
        else: pass
    send_count = send_count-1
    wait_count = wait_count-1
    if (wait_count==0 & receive_flag = False):
        display.show(Image.SKULL)
        wait_count = 1
    if (send_count == 0 & receive_flag == True):
        send_count = 1000
    elif (send_count == 0):
        send_count = 1 //loop to send_count

    #reset button
    if (button_a.is_pressed()):
        send_count = 1000
        wait_count = 500
    sleep(10)
