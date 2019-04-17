---
@title[Title]

@size[2.7em](Remote-Controlling a Smart TV with HTTP)

Alexander Stedile, Bernhard (Slash) Trummer

Presented at Grazer Linuxtage 2019

---

## Abstract

Remote-Controlling a Smart TV with HTTP

A pure programming project, no device needs to be built.
A little bit of network engineering for research.

---

## Presentation Overview

- Motivation
- Aim
- Research
- API, Protocol
- Application lgremote
- Lessons Learned

---

## Motivation

The TV has so many ways to interact with it. 

How can I get access to it for building my own remote control features?

+++

### Smart TV Connectivity 

LG Smart TV 42LA6608-ZA
* Broadcast: DVB-T/C/S
* Video: HDMI, Scart, Chinch
* USB
* LAN, WLAN
* Remote control: Infrared, Radio (Magic Remote), Android App

---

## Aim

I want to be able to invent and try out new features 
for a smarter remote control. 
It should be easy to add and modify functionality.

---

## Research

+++

### Packet Sniffing

* WLAN network setup
* tcpdump
* Wireshark

+++

### Internet Research

---

## ROAP - API, Protocol

---

## Application lgremote

+++

### Scope

+++

### Out of Scope
* Device discovery phase
* Digital video recorder
* Teletext

+++

### Features

+++

### Ideas for Further Features
* On/off toggle for skipping channels
* Look if commercials are finished
* Repeatedly query current program info

---

## Lessons Learned
* Current volume leven cannot be queried.
* Uplink to Internet is required for TV to accept network connection.
* Turning off TV takes 15 seconds if "byebye" event cannot be sent to connected client.
* For selecting a channel, 4 parameters have to be sent. (Can be queried with channel list.)

---

## References

* lgremote project: https://github.com/astedile/lgremote
* LG TV 2012 mit deviceinfo steuern, funktioniert in Neo - https://homematic-forum.de/forum/viewtopic.php?t=29820

---

## Summary

---

## Feedback

Is very welcome. Talk to me today 
or use the feedback feature on www.linuxtage.at

