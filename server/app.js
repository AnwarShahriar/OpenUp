const express = require('express')
const bodyParser = require('body-parser')
const localIP = require('my-local-ip')
const app = express()
const qrcode = require('qrcode-terminal')
const open = require('open')
const getUrls = require('get-urls')

app.use(bodyParser.json())

app.post('/openup', (req, res) => {
    let msg;
    if (req.body.url) {
        msg = 'Will try to open up url with default browser'
        let urlSet = getUrls(req.body.url)
        console.log(urlSet)
        if (urlSet.size) {
            console.log(urlSet.entries().next().value[0])
            open(urlSet.entries().next().value[0])
        }
    } else {
        msg = 'Please, give me a url'
    }

    console.log(msg)
    res.status(200).json({ msg })
})

const port = process.env.PORT || 3000
app.listen(port, () => {
    console.log(`Listening to port ${port}`)
    showLocalIP()
})

const showLocalIP = () => {
    const ip = localIP()
    if (ip) {
        console.log(`Your local ip is: ${ip}`)
        console.log('Scan below QR code with your mobile to connect')
        qrcode.generate(ip, (qr) => {
            console.log(qr)
        });
    } else {
        console.log("Please, connect to a wifi network and retry")
    }
}
