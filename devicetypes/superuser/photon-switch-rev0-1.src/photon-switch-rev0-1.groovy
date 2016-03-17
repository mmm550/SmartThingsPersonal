/**
 *  Photon Switch
 *
 *  Author: keithcroshaw@gmail.com
 *  Date: 2016-01-26
 *
 *	Thanks to justinwurth@gmail.com for original Device Handler
 */
 
metadata {
	definition (name: "Photon Switch Rev0.1", author: "keithcroshaw@gmail.com") {
		capability "Switch"
        capability "Relay Switch"
		command "onPhysical"
		command "offPhysical"
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
	tiles(scale: 2) {
		multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true){
			tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
				attributeState "on", label: '${currentValue}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821"
				attributeState "off", label: '${currentValue}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
			}
		}
		standardTile("on", "device.switch", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'On', action: "onPhysical", backgroundColor: "#ffffff"
		}
		standardTile("off", "device.switch", width: 2, height: 2, decoration: "flat") {
			state "default", label: 'Off', action: "offPhysical", backgroundColor: "#ffffff"
		}
        main "switch"
		details(["switch","on","off"])
    }
}

def parse(String description) {
	def pair = description.split(":")
	createEvent(name: pair[0].trim(), value: pair[1].trim())
}

def on() {
	put '1'
    //sendEvent (name: "switch", value: "on", isStateChange:true)
}

def off() {
	put '0'
    //sendEvent (name: "switch", value: "off", isStateChange:true)
}

def onPhysical() {
	put '1'
	log.debug "$version onPhysical()"
	sendEvent(name: "switch", value: "on", type: "physical")
}

def offPhysical() {
	put '0'
	log.debug "$version offPhysical()"
	sendEvent(name: "switch", value: "off", type: "physical")
}

private put(led) {
    //Spark Core API Call
	httpPost(
		uri: "https://api.spark.io/v1/devices/${deviceId}/${sparkFunc}",
        body: [access_token: token, command: led],  
	) {response -> log.debug (response.data)}
}