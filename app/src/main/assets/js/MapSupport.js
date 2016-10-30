"use strict";
var tmp_lon_x;
var tmp_lat_y;
var thi, abs;
var which_mapTypeId = 1; //2,3 not yet

var myIcon = [];
var marker = [];
var infowindow = [];

var map;
var myTrip = [];

var ITRI_location = new google.maps.LatLng(23.5832, 120.5825);

function initialize() {
    var mapProp = {
        center: ITRI_location,
        zoom: 8,
        //mapTypeId: google.maps.MapTypeId.ROADMAP //ROADMAP, SATELLITE, HYBRID, or TERRAIN
    };
    switch (which_mapTypeId) {
        case 1:
            mapProp.mapTypeId = google.maps.MapTypeId.ROADMAP
            break;
        case 2:
            mapProp.mapTypeId = google.maps.MapTypeId.SATELLITE
            break;
        case 3:
            mapProp.mapTypeId = google.maps.MapTypeId.HYBRID
            break;
        default:
            mapProp.mapTypeId = google.maps.MapTypeId.ROADMAP
            break;
    }
    map = new google.maps.Map(document.getElementById("googleMap"), mapProp);
}

function locateMap(lat, lng, zoom) {
    var pt = new google.maps.LatLng(lat, lng);
    map.setCenter(pt);
    map.setZoom(zoom);
}
var tripPath = [];
var tripIcons = [];
var tripMarker = [];
var tripLine;
var tripLines = [];
var spotMarker;

function clearPath() {
    for (var i = 0; i < tripMarker.length; i++) {
        tripMarker[i].setMap(null);
    }
    tripLine.setMap(null);
}


function addSpots(spotArr) {
    spotMarker = [];
    for (var i = 0; i < spotArr.length; i++) {
        let curloc = spotArr[i];

        spotMarker[i] = new google.maps.Marker({
            position: new google.maps.LatLng(curloc["latitude"], curloc["longitude"]),
//            icon: new google.maps.MarkerImage("img/car_red.png", null, null, null, new google.maps.Size(60, 60)),
            icon: new google.maps.MarkerImage(curloc["img_url"], null, null, null, new google.maps.Size(60, 60)),
            title: curloc["name"]
        });
        spotMarker[i].addListener('click', function () {
            map.setZoom(20);
            map.setCenter(this.getPosition());

            var infowindow = new google.maps.InfoWindow({
                content: '<h3>'+curloc["name"] + '</h3><br><img src="' + curloc["img_url"] + '" alt="Spot View" style="width:304px;height:228px;"><br>' + curloc["description"]
            });
            infowindow.open(map, this);
        });

        spotMarker[i].setMap(map);
    }
}

function addNode(curloc) {
	var idx = tripPath.length;
	
	tripPath[idx] = new google.maps.LatLng(curloc["latitude"], curloc["longitude"]);
	
	
	if (curloc["vehicle_speed"] < 50) {
		tripIcons[idx] = new google.maps.MarkerImage("img/car_red.png", null, null, null, new google.maps.Size(30, 30));
	} else if (curloc["vehicle_speed"] >= 50 && curloc["vehicle_speed"] <= 80) {
		tripIcons[idx] = new google.maps.MarkerImage("img/car_yellow.png", null, null, null, new google.maps.Size(30, 30));
	} else {
		tripIcons[idx] = new google.maps.MarkerImage("img/car_green.png", null, null, null, new google.maps.Size(30, 30));
	}


	tripMarker[idx] = new google.maps.Marker({
		position: tripPath[idx],
		icon: tripIcons[idx],
		title: curloc["vehicle_speed"] + "km"
	});

	tripMarker[idx].addListener('click', function () {
		map.setZoom(20);
		map.setCenter(this.getPosition());
	});

	tripMarker[idx].setMap(map);
	
	if(idx > 0){ //link pre-node
		var tmpLine = new google.maps.Polyline({
			path: [tripPath[idx - 1], tripPath[idx]],
			strokeColor: "#0000FF",
			strokeOpacity: 0.8,
			strokeWeight: 2,
			fillColor: "#0000FF",
			fillOpacity: 0.4
		});
		tmpLine.setMap(map);
	}
	
	locateMap(curloc["latitude"], curloc["longitude"], 20);
}



function addPath(locArr) {
    tripPath = [];
    tripIcons = [];
    tripMarker = [];

    //build all path
    for (var i = 0; i < locArr.length; i++) {
        tripPath[i] = new google.maps.LatLng(locArr[i]["latitude"], locArr[i]["longitude"]);
    }

    locateMap(locArr[0]["latitude"], locArr[0]["longitude"], 20);

    //draw all path
    tripLine = new google.maps.Polyline({
        path: tripPath,
        strokeColor: "#0000FF",
        strokeOpacity: 0.8,
        strokeWeight: 2,
        fillColor: "#0000FF",
        fillOpacity: 0.4
    });
    tripLine.setMap(map);


    for (var i = 0; i < locArr.length; i++) {
        var curloc = locArr[i];
        if (curloc["vehicle_speed"] < 50) {
            tripIcons[i] = new google.maps.MarkerImage("img/car_red.png", null, null, null, new google.maps.Size(30, 30));
        } else if (curloc["vehicle_speed"] >= 50 && curloc["vehicle_speed"] <= 80) {
            tripIcons[i] = new google.maps.MarkerImage("img/car_yellow.png", null, null, null, new google.maps.Size(30, 30));
        } else {
            tripIcons[i] = new google.maps.MarkerImage("img/car_green.png", null, null, null, new google.maps.Size(30, 30));
        }


        tripMarker[i] = new google.maps.Marker({
            position: tripPath[i],
            icon: tripIcons[i],
            title: curloc["vehicle_speed"] + "km"
        });

        tripMarker[i].addListener('click', function () {
            map.setZoom(20);
            map.setCenter(this.getPosition());
        });

        tripMarker[i].setMap(map);
    }
}

google.maps.event.addDomListener(window, 'load', initialize);