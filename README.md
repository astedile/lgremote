# lgremote
Java application with Swing UI for remote-controlling an LG smart TV using it's ROAP interface.

## Specifications
This application is tested with 
- Java 1.8.0_201
- Maven 3.3.9
- LG Smart TV 42LA6608-ZA

## How to run
Run with Maven
```
mvn spring-boot:run <options>
```

Command line options:
- `--tvIpAddress=` ... TV's IP address. Can also be entered in GUI. (optional)
- `--tvAuthKey=` ... TV's authentication key. Can also be entered in GUI. (optional)
- `--favouriteChannelList=` ... Favourite channel list that should be selected. 
Will be checked at startup. (optional, default is A)
- `--favouriteChannelListExpectedSize=` ... Number of channels in favourite channel list.
Used to detect if the chosen favouriteChannelList is currently selected. (optional)
