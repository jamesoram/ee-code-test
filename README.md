**Summary of exploratory testing session:**

*Main findings*

* There are no clear instructions on what the website is for, and the branding does not make it clear
to whom it belongs.
* Unencrypted HTTP (which is a no-no for e-commerce)
* No privacy policy or cookie message
* Both front-end and back-end are not validating input
* There is information missing in the form: email, currency, type of room etc.
* There is a JS error in the console

*The following is a summary of the session in braindump format*

The first thing I did was check for cookies and local storage. I couldn't find any on landing.
Next, I clicked the "Save" button without entering any data and got an HTTP 500 response from the back-end. This is a defect as 
the front-end and back-end should validate data. This can be reproduced simply with an HTTP client, for example curl:

```
 % curl -vv -X POST 'http://hotel-test.equalexperts.io/booking'
*   Trying 52.48.14.29...
* TCP_NODELAY set
* Connected to hotel-test.equalexperts.io (52.48.14.29) port 80 (#0)
> POST /booking HTTP/1.1
> Host: hotel-test.equalexperts.io
> User-Agent: curl/7.54.0
> Accept: */*
>
< HTTP/1.1 500 Internal Server Error
< Content-Type: text/plain; charset=utf-8
< Date: Tue, 03 Oct 2017 18:10:49 GMT
< ETag: W/"15-3JQVFLwoG6yepWGqlDPA/A"
< X-Powered-By: Express
< Content-Length: 21
< Connection: keep-alive
<
* Connection #0 to host hotel-test.equalexperts.io left intact
Internal Server Error
```
This tells me it's an Express application, which is interesting.

Looking at the JS console I see the following error message:

```
script.js:15 Uncaught TypeError: Cannot read property 'bookingid' of undefined
    at getBooking (script.js:15)
    at Object.success (script.js:26)
    at i (jquery-2.2.3.min.js:2)
    at Object.fireWith [as resolveWith] (jquery-2.2.3.min.js:2)
    at z (jquery-2.2.3.min.js:4)
    at XMLHttpRequest.<anonymous> (jquery-2.2.3.min.js:4)
getBooking @ script.js:15
(anonymous) @ script.js:26
i @ jquery-2.2.3.min.js:2
fireWith @ jquery-2.2.3.min.js:2
z @ jquery-2.2.3.min.js:4
(anonymous) @ jquery-2.2.3.min.js:4
XMLHttpRequest.send (async)
(anonymous) @ VM277:1
send @ jquery-2.2.3.min.js:4
ajax @ jquery-2.2.3.min.js:4
n.(anonymous function) @ jquery-2.2.3.min.js:4
populateBookings @ script.js:10
(anonymous) @ script.js:2
i @ jquery-2.2.3.min.js:2
fireWith @ jquery-2.2.3.min.js:2
ready @ jquery-2.2.3.min.js:2
J @ jquery-2.2.3.min.js:2
```

It looks like it is trying to get a Booking ID via an AJAX call and failing. 
This is also an issue that needs to be reported.

Taking a step back, it looks like the site would belong to an actual hotel,
rather than being a white label site. I assume this based on the fact that there is no location.
This makes me wonder if this hotel only has one type of room, as it seems like you can't specify 
what type of room you want.

Following that, I see that it also only allows one name and surname. This could cause issues such 
as people with the same name being confused by hotel personnel or problems with people from countries 
that have other naming conventions. For example, Spanish people have two surnames.

That deposit dropdown probably should have been a checkbox, and I wonder what currency that Price box is assuming
I'm going to use.

I then proceeded to fill in all the textboxes in the form, and put the same date for check-in and check-out.
Surprisingly, the application doesn't have a problem with this (also an issue) and I wondered how I would get my 
confirmation. Perhaps my email address should also be on the form? I clicked delete and it deleted my booking correctly.

After that, I made a booking with dates in the past that was also accepted.

My next move is to try a SQL injection, but it looks like the application is correctly sanitising input.
My first name is:

``` SQL
' OR 1=1; DROP TABLE USERS;
```
Just to make sure, I try to inject Javascript. It looks like if I use the script HTML tag the application breaks, 
as it does not display anything.

There is no SSL certificate, which means I would probably never pay anything on this site :) 
There is also no privacy policy and no cookie message, which could lead to legal problems.

Finally, as my exploratory testing timebox is finishing, I try to input letters into the price box. The server responds
with an HTTP 500 error. This is an issue as both the front-end and the back-end should validate the data.

