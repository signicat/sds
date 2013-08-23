// HTTP POST PDF to Signicat SDS
// using Node.js (http://nodejs.org)
// and the request library (https://github.com/mikeal/request)

var fs = require('fs');
var request = require('request');

fs.createReadStream('mydocument.pdf').pipe(

	request.post(
	    'https://test.signicat.com/doc/shared/sds/',
	    function (error, response, body) {
	        if (!error && response.statusCode == 201) {
	            console.log(body);
	        }
	        else {
	        	console.log(error);
	        }
	    }
	).auth(
		"shared",
		"Bond007"
	)

);	