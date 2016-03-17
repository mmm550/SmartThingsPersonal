/**

 *  Particle Photon Blinds

 *

 *  Author: Keith Croshaw

 *  Date: Feb 02 2016

 */

 

 preferences {

    input("deviceId", "text", title: "Device ID")

    input("token", "text", title: "Access Token")
    
    input("particleFunction", "text", title: "Particle Function")

}

 

 // for the UI

metadata {

    // Automatically generated. Make future change here.

    definition (name: "Particle Photon Blinds RevA", author: "justinwurth@gmail.com") {
		
        capability "Switch Level"
        capability "Switch"

    }


    // tile definitions

    tiles {
		standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {

			state "on", label:'${name}', action:"switch.off", icon:"st.switches.switch.on", backgroundColor:"#79b821"
			state "off", label:'${name}', action:"switch.on", icon:"st.switches.switch.off", backgroundColor:"#ffffff"

		}
		
		standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
			state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
		}
		
		main(["switch"])
		details(["switch", "refresh"])
	}


}


def parse(String description) {

    log.error "This device does not support incoming events"

    return null

}


def on() {

    put '1'
    
}


def off() {

    put '0'

}

private put(particleFunction) {

    //Particle Photon API Call

//log.debug "https://api.particle.io/v1/devices/${deviceId}/${particleFunction}"
    httpPost(

        uri: "https://api.particle.io/v1/devices/${deviceId}/${particleFunction}",

        body: [access_token: token, command: particleFunction],  

    ) {response -> log.debug (response.data)}

}