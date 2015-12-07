/**
 *  Spark Core
 *
 *  Author: juano23@gmail.com
 *  Date: 2014-01-23
 */
 
metadata {
	definition (name: "Photon RC Switch 2", author: "badgermanus@gmail.com") {
		capability "Switch"
        capability "Momentary"
        capability "Refresh"
	}
 
preferences {
    input("deviceId", "text", title: "Device ID")
    input("token", "text", title: "Access Token")
    input("sparkFunc", "text", title: "Spark Function Name")
}

    // simulator metadata
    simulator {
        status "on":  "command: 2003, payload: FF"
        status "off": "command: 2003, payload: 00"

        // reply messages
        reply "2001FF,delay 100,2502": "command: 2503, payload: FF"
        reply "200100,delay 100,2502": "command: 2503, payload: 00"
    }

    // tile definitions
    tiles {
        standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
            state "on", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821"
            state "off", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
        }
		standardTile("push", "device.switch", inactiveLabel: false, decoration: "flat") {
            state "default", label:'Push', action:"momentary.push", icon: "st.secondary.refresh-icon"
        }
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
            state "default", label:'Refresh', action:"device.refresh", icon: "st.secondary.refresh-icon"
        }

        main "switch"
        details(["switch","push","refresh"])
    }
}

def parse(String description) {
	log.error "This device does not support incoming events"
	return null
}

def on() {
	put '1'
    //sendEvent (name: "switch", value: "on", isStateChange:true)
}

def off() {
	put '0'
    //sendEvent (name: "switch", value: "off", isStateChange:true)
}

private put(led) {
    //Spark Core API Call
	httpPost(
		uri: "https://api.spark.io/v1/devices/${deviceId}/${sparkFunc}",
        body: [access_token: token, command: led],  
	) {response -> log.debug (response.data)}
}

def push() {
	log.debug "Push"
    sendEvent(name: "momentary", value: "push", isStateChange:true)
}