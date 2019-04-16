## Remote-Controlling a Smart TV with HTTP

Alexander Stedile\
Bernhard Trummer

Presented at Grazer Linuxtage 2019

---

## Abstract
TODO: Translate

"Reines Software-Projekt, keine HW-Basteleien. Ein bisschen NW-Technik für Forschung."

---

## Overview

+++

### Table of Contents

---

## Motivation

+++

### Smart TV Connectivity 

---

## Aim

---

## Research

+++

### Packet Sniffing
WLAN network setup
tcpdump
Screenshot Wireshark

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
* LG TV 2012 mit deviceinfo steuern, funktioniert in Neo - https://homematic-forum.de/forum/viewtopic.php?t=29820

---

## Summary

---

## Feedback

