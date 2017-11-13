const localIP = require('my-local-ip')

const ip = localIP()
if (ip) {
    console.log(localIP())
} else {
    console.log("Please, connect to a wifi network and retry")
}
