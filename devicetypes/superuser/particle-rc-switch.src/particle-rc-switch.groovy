/**
 *  Spark Core
 *
 *  Author: juano23@gmail.com
 *  Date: 2014-01-23
 */
 
 preferences {
    input("deviceId", "text", title: "Device ID")
    input("token", "text", title: "Access Token")
    input("sparkFunc", "text", title: "Spark Function Name")
}
 
 // for the UI
metadata {
    // Automatically generated. Make future change here.
	definition (name: "Particle RC-Switch", author: "keithcroshaw@gmail.com") {
    	capability "Switch"
	}

    // tile definitions
	tiles {
		standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
			state "on", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#79b821"
			state "off", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
		}
        

		main "switch"
		details "switch"
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