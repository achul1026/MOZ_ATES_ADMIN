const Env = {
    icons : [
        {"url" : "/images/facility_TFT001.png", "id" : "TFT001"},
        {"url" : "/images/facility_TFT002.png", "id" : "TFT002"},
        {"url" : "/images/facility_TFT003.png", "id" : "TFT003"},
        {"url" : "/images/facility_TFT004.png", "id" : "TFT004"},
        {"url" : "/images/facility_TFT005.png", "id" : "TFT005"},
        {"url" : "/images/enfrocement_ENF001.png", "id" : "LTC000"},
        {"url" : "/images/enfrocement_ENF001.png", "id" : "LTC001"},
        {"url" : "/images/enfrocement_ENF001.png", "id" : "LTC002"},
        {"url" : "/images/enfrocement_ENF001.png", "id" : "LTC003"},
        {"url" : "/images/enfrocement_ENF001.png", "id" : "LTC004"},
        {"url" : "/images/enfrocement_ENF001.png", "id" : "LTC005"},
        {"url" : "/images/enfrocement_ENF001.png", "id" : "LTC006"},
        {"url" : "/images/enfrocement_ENF001.png", "id" : "LTC007"},
        {"url" : "/images/accident_ACT001.png", "id" : "ACT000"},
        {"url" : "/images/accident_ACT001.png", "id" : "ACT001"},
        {"url" : "/images/accident_ACT001.png", "id" : "ACT002"},
        {"url" : "/images/accident_ACT001.png", "id" : "ACT003"},
        {"url" : "/images/accident_ACT001.png", "id" : "ACT004"},
        {"url" : "/images/accident_ACT001.png", "id" : "ACT005"},
        {"url" : "/images/accident_ACT001.png", "id" : "ACT006"},
        {"url" : "/images/accident_ACT001.png", "id" : "ACT007"},
        {"url" : "/images/equipment_EQP001.png", "id" : "EQT001"}
    ],
    source : {
        warning : "MOZ_ATES_WARNING",
        enforcement : "MOZ_ATES_ENFORCEMENT",
        accident : "MOZ_ATES_ACCIDENT",
        facility : "MOZ_ATES_FACILITY",
        equipment : "MOZ_ATES_EQUIPMENT"
    },
    layer : {
        warning : "MOZ_ATES_WARNING",
        enforcement : "MOZ_ATES_ENFORCEMENT_LAYER",
        accident : "MOZ_ATES_ACCIDENT_LAYER",
        facility : "MOZ_ATES_FACILITY_LAYER",
        equipment : "MOZ_ATES_EQUIPMENT_LAYER"
    }
}
/**
 * MozAtes Map Core js
 * @param elementId
 * @param center_lng
 * @param center_lat
 * @param useGeoLocation
 * @param isInitDrawCenterMarker
 * @returns {MozAtesMap}
 * @constructor
 */
const MozAtesMap = function({elementId, center_lng, center_lat, useGeoLocation = false ,isInitDrawCenterMarker,isFacility = false , isEquipment = false, isEnforcement = false, isAccident = false}){
    const _core = this;
    const _pbkey = "pk.eyJ1IjoiZGVzaW1pbjIiLCJhIjoiY2xvbzMwN2t3Mm52dzJrcXR6em5lZ3hmMyJ9.pu7IdtCJVHme2QXzu4sT7w";
    let _center = center_lng && center_lat ? [center_lng, center_lat] : [32.609310,-25.907068];
    let _map = null;
    let _userLng = null;
    let _userLat = null;
    let _geoLocationTrigger = useGeoLocation;
    let _geoLocate = null;
    const popup = new mapboxgl.Popup({
        closeButton: false,
        closeOnClick: false,
        maxWidth : "none"
    });
    let loadSource = async function(){
        for(const icon of Env.icons) {
            await _map.loadImage(icon.url, function(err, image){
                if(icon.sdf === true) {
                    _map.addImage(icon.id, image, {sdf : true});
                }else{
                    _map.addImage(icon.id, image);
                }
            });
        }
    }
    
    function handlePermission() {
        navigator.permissions.query({name:'geolocation'}).then(function(result) {
            if (result.state === 'granted') {
                getGelocation();
            } else if (result.state === 'prompt') {
                report(result.state);
                getGelocation();
            } else if (result.state === 'denied') {
                report(result.state);
            }
            result.addEventListener('change', function() {
                report(result.state);
            });
        });
    }
    function report(state) {
        console.log('Permission ' + state);
    }
//	handlePermission();
    // Geolocation API에 액세스할 수 있는지를 확인
    function getGelocation(){
        if (navigator.geolocation) {
            //위치 정보를 얻기
            navigator.geolocation.getCurrentPosition (function(pos) {
                if(typeof pos == "undefined") {
                    alert("Not support geolocation.");
                    return
                }
                _userLat = pos.coords.latitude;
                _userLng = pos.coords.longitude;
                _center = [pos.coords.longitude,pos.coords.latitude];
            });
        } else {
//			alert("Not support geolocation.")
        }
    }
    if(_geoLocationTrigger) getGelocation();

    _core.trigger = function(){
        _geoLocationTrigger = true;
    }
    _core.addGeolocate = function(){
        let geolocate = new mapboxgl.GeolocateControl({
            positionOptions: {
                enableHighAccuracy: true
            },
            trackUserLocation: true,
            showUserHeading: true
        })
        _map.addControl(geolocate, "bottom-right");
        geolocate.on('geolocate', (event) => {
            _userLng = event.coords.longitude;
            _userLat = event.coords.latitude;
        });
        return geolocate;
    }

    _core.init = function(){
        const element = document.getElementById(elementId);
        if(!element) {
            console.error("Not found element.");
            return;
        }
        mapboxgl.accessToken = _pbkey;
        _map = new mapboxgl.Map({
            container: elementId, // container ID
            style: 'mapbox://styles/mapbox/streets-v12', // style URL
            center: _center, // starting position [lng, lat]
            zoom: 11, // starting zoom
        });
        if(_geoLocationTrigger) _geoLocate = _core.addGeolocate();
        loadSource().then();
        _map.on('load', function() {
            // user location tracking
            if(_geoLocationTrigger) _geoLocate?.trigger();
            if(isInitDrawCenterMarker && center_lng && center_lat) {
                _core.drawMarker([center_lng, center_lat]);
            }
            
            if(isEquipment){
	            _core.equipment = new Equipment();
	
	            _core.equipment.getSource().then(()=>{
	                _core.equipment.drawEquipment('EQT001');
	            });
			}

            if(isFacility){
	            _core.facility = new Facility();
	
	            _core.facility.getSource().then(()=>{
	                _core.facility.drawFacility('TFT001');
	                _core.facility.drawFacility('TFT002');
	                _core.facility.drawFacility('TFT003');
	                _core.facility.drawFacility('TFT004');
	                _core.facility.drawFacility('TFT005');
	                _core.facility.drawFacility('TFT006');
	                _core.facility.drawFacility('TFT007');
	                _core.facility.drawFacility('TFT008');
	            });
			}
			
			if(isEnforcement){
				_core.enforcement = new Enforcement();
	
	            _core.enforcement.getSource().then(()=>{
	                _core.enforcement.drawEnforcement('LTC000');
	                _core.enforcement.drawEnforcement('LTC001');
	                _core.enforcement.drawEnforcement('LTC002');
	                _core.enforcement.drawEnforcement('LTC003');
	                _core.enforcement.drawEnforcement('LTC004');
	                _core.enforcement.drawEnforcement('LTC005');
	                _core.enforcement.drawEnforcement('LTC006');
	                _core.enforcement.drawEnforcement('LTC007');
	            });
			}
			
			if(isAccident){
				_core.accident = new Accident();
	
	            _core.accident.getSource().then(()=>{
	                _core.accident.drawAccident('ACT000');
	                _core.accident.drawAccident('ACT001');
	                _core.accident.drawAccident('ACT002');
	                _core.accident.drawAccident('ACT003');
	                _core.accident.drawAccident('ACT004');
	                _core.accident.drawAccident('ACT005');
	                _core.accident.drawAccident('ACT006');
	                _core.accident.drawAccident('ACT007');
	            });
			}
			
        });
    }
    
    _core.flyToMove = function(lng , lat){
		_map.flyTo({
			center: [lng, lat],
	        zoom: 14
	    })        
	}
    
    _core.drawIcon = function(icon, sourceName,layerName, filter, callback){
		
        if(typeof _map.getLayer(layerName) !== "undefined") return;
        let obj = {
            'id': layerName,
            'type': 'symbol',
            'source': sourceName,
            'maxzoom': 22,
            'minzoom': 9,
            'layout': {
                'visibility' : "visible",
                'icon-allow-overlap': true,
                'icon-image': icon,
                "icon-size": [
                    'interpolate',
                    ['linear'],
                    ['zoom'],
                    10, 0.3,
                    15, 0.55
                ]
            }
        }
        if(filter){
            obj.filter = filter;
        }
        _map.addLayer(obj);
        if(typeof callback === 'function') typeof callback(obj);
    }
    _core.toggleLayer = function(layer){
        if(!_map.getLayer(layer)){
            return false;
        }
        if(_map.getLayer(layer))
            if(_map.getLayoutProperty(layer, 'visibility') === "none"){
                _map.setLayoutProperty(layer, 'visibility', 'visible');
            }else{
                _map.setLayoutProperty(layer, 'visibility', 'none');
            }
    }
    _core.hideLayer = function(layer){
        _map.setLayoutProperty(layer, 'visibility', 'none');
    }
    _core.drawMarker = function(lngLat){
        new mapboxgl.Marker({ color: 'red'})
            .setLngLat(lngLat)
            .addTo(_map);
    }
    _core.getLng = function(){
        return _userLng;
    }

    _core.getLat = function(){
        return _userLat;
    }
    _core.init();
    
    class Enforcement{
        constructor() {

        }

        async getSource() {
            await fetch("/eqp/mng/enfMngGeojson.ajax", {})
                .then((response) => {
                    return response.json()
                })
                .then((geoJson)=>{
                    this.geoJson = geoJson;
                    if(_map.getSource(Env.source.enforcement)){
                        _map.getSource(Env.source.enforcement).setData(geoJson);
                    }else{
                        _map.addSource(Env.source.enforcement, {
                            type : "geojson",
                            data : geoJson
                        });
                    }
                })
                .catch((err)=>{
                    console.error(err);
                    // alert("An error occurred while retrieving facility information. Please contact the administrator");
                })
        }
        toggleEnforcementLayer = function(typeCode) {
			cosole.log(typeCode)
            _core.toggleLayer(Env.layer.enforcement+"_"+typeCode);
        }
        drawEnforcement = function(typeCode){
            const _this = this;
            _core.drawIcon(['get','CD_ID'],Env.source.enforcement,Env.layer.enforcement+"_"+typeCode, ['==','CD_ID',typeCode], function(...layerObj){
                for(const obj of layerObj){
                    _map.off("mouseenter",obj.id,_this.hoverEvent);
                    _map.off("mouseleave",obj.id,_this.leaveEvent);
                    _map.on("mouseenter",obj.id,_this.hoverEvent);
                    _map.on("mouseleave",obj.id,_this.leaveEvent);
                }
            });
        }
        hoverEvent(e) {
            _map.getCanvas().style.cursor = 'pointer';

            // Copy coordinates array.
            const coordinates = e.features[0].geometry.coordinates.slice();
            const prop = e.features[0].properties;
			
            const html = `
                <div class="mkPopWrap">
                    <div class="mkCon">
                        <p class="mkTitle">Case No.</p>
                        <p>${prop.TFC_ENF_ID}</p>
                    </div>
                    <div class="mkCon">
                        <p class="mkTitle">Type</p>
                        <p>${prop.CD_NM}</p>
                    </div>
                    <div class="mkCon">
                        <p class="mkTitle">Road</p>
                        <p>${prop.ROAD_ADDR}</p>
                    </div>
                    <div class="mkCon">
                        <p class="mkTitle">Status</p>
                        <p>${prop.FACILITY_STTS === 'Y' ? 'Activate' : 'Deactivate'}</p>
                    </div>
                </div>
            `
            while (Math.abs(e.lngLat.lng - coordinates[0]) > 180) {
                coordinates[0] += e.lngLat.lng > coordinates[0] ? 360 : -360;
            }

            // Populate the popup and set its coordinates
            // based on the feature found.
            popup.setLngLat(coordinates).setHTML(html).addTo(_map);
        }
        leaveEvent() {
            _map.getCanvas().style.cursor = '';
            popup.remove();
        }
    }
    
    class Accident{
        constructor() {

        }

        async getSource() {
            await fetch("/eqp/mng/actMngGeojson.ajax", {})
                .then((response) => {
                    return response.json()
                })
                .then((geoJson)=>{
                    this.geoJson = geoJson;
                    if(_map.getSource(Env.source.accident)){
                        _map.getSource(Env.source.accident).setData(geoJson);
                    }else{
                        _map.addSource(Env.source.accident, {
                            type : "geojson",
                            data : geoJson
                        });
                    }
                })
                .catch((err)=>{
                    console.error(err);
                    // alert("An error occurred while retrieving facility information. Please contact the administrator");
                })
        }
        toggleAccidentLayer = function(typeCode) {
            _core.toggleLayer(Env.layer.accident+"_"+typeCode);
        }
        drawAccident = function(typeCode){
            const _this = this;
            _core.drawIcon(['get','CD_ID'],Env.source.accident,Env.layer.accident+"_"+typeCode, ['==','CD_ID',typeCode], function(...layerObj){
                for(const obj of layerObj){
                    _map.off("mouseenter",obj.id,_this.hoverEvent);
                    _map.off("mouseleave",obj.id,_this.leaveEvent);
                    _map.on("mouseenter",obj.id,_this.hoverEvent);
                    _map.on("mouseleave",obj.id,_this.leaveEvent);
                }
            });
        }
        hoverEvent(e) {
            _map.getCanvas().style.cursor = 'pointer';

            // Copy coordinates array.
            const coordinates = e.features[0].geometry.coordinates.slice();
            const prop = e.features[0].properties;
			
            const html = `
                <div class="mkPopWrap">
                    <div class="mkCon">
                        <p class="mkTitle">Case No.</p>
                        <p>${prop.TFC_ACDNT_ID}</p>
                    </div>
                    <div class="mkCon">
                        <p class="mkTitle">Type</p>
                        <p>${prop.CD_NM}</p>
                    </div>
                    <div class="mkCon">
                        <p class="mkTitle">Road</p>
                        <p>${prop.ROAD_ADDR}</p>
                    </div>
                    <div class="mkCon">
                        <p class="mkTitle">Status</p>
                        <p>${prop.FACILITY_STTS === 'Y' ? 'Activate' : 'Deactivate'}</p>
                    </div>
                </div>
            `
            while (Math.abs(e.lngLat.lng - coordinates[0]) > 180) {
                coordinates[0] += e.lngLat.lng > coordinates[0] ? 360 : -360;
            }

            // Populate the popup and set its coordinates
            // based on the feature found.
            popup.setLngLat(coordinates).setHTML(html).addTo(_map);
        }
        leaveEvent() {
            _map.getCanvas().style.cursor = '';
            popup.remove();
        }
    }
    
    class Facility{
        constructor() {

        }

        async getSource() {
            await fetch("/eqp/mng/facMngGeojson.ajax", {})
                .then((response) => {
                    return response.json()
                })
                .then((geoJson)=>{
                    this.geoJson = geoJson;
                    if(_map.getSource(Env.source.facility)){
                        _map.getSource(Env.source.facility).setData(geoJson);
                    }else{
                        _map.addSource(Env.source.facility, {
                            type : "geojson",
                            data : geoJson
                        });
                    }
                })
                .catch((err)=>{
                    console.error(err);
                    // alert("An error occurred while retrieving facility information. Please contact the administrator");
                })
        }
        toggleFacilityLayer = function(typeCode) {
            _core.toggleLayer(Env.layer.facility+"_"+typeCode);
        }
        drawFacility = function(typeCode){
            const _this = this;
            _core.drawIcon(['get','CD_ID'],Env.source.facility,Env.layer.facility+"_"+typeCode, ['==','CD_ID',typeCode], function(...layerObj){
                for(const obj of layerObj){
                    _map.off("mouseenter",obj.id,_this.hoverEvent);
                    _map.off("mouseleave",obj.id,_this.leaveEvent);
                    _map.on("mouseenter",obj.id,_this.hoverEvent);
                    _map.on("mouseleave",obj.id,_this.leaveEvent);
                }
            });
        }
        hoverEvent(e) {
            _map.getCanvas().style.cursor = 'pointer';

            // Copy coordinates array.
            const coordinates = e.features[0].geometry.coordinates.slice();
            const prop = e.features[0].properties;
			
			
            const html = `
                <div class="mkPopWrap">
                    <div class="mkCon">
                        <p class="mkTitle">Type</p>
                        <p>${prop.CD_NM}</p>
                    </div>
                    <div class="mkCon">
                        <p class="mkTitle">Name</p>
                        <p>${prop.FACILITY_NM}</p>
                    </div>
                    <div class="mkCon">
                        <p class="mkTitle">Road</p>
                        <p>${prop.ROAD_ADDR}</p>
                    </div>
                    <div class="mkCon">
                        <p class="mkTitle">Status</p>
                        <p>${prop.FACILITY_STTS === 'Y' ? 'Activate' : 'Deactivate'}</p>
                    </div>
                </div>
            `
            while (Math.abs(e.lngLat.lng - coordinates[0]) > 180) {
                coordinates[0] += e.lngLat.lng > coordinates[0] ? 360 : -360;
            }

            // Populate the popup and set its coordinates
            // based on the feature found.
            popup.setLngLat(coordinates).setHTML(html).addTo(_map);
        }
        leaveEvent() {
            _map.getCanvas().style.cursor = '';
            popup.remove();
        }
    }
    
    /** 단속 장비 시작*/
    class Equipment{
        constructor() {

        }
        async getSource() {
            await fetch("/eqp/mng/eqpMngGeojson.ajax", {})
                .then((response) => {
                    return response.json()
                })
                .then((geoJson)=>{
                    this.geoJson = geoJson;
                    if(_map.getSource(Env.source.equipment)){
                        _map.getSource(Env.source.equipment).setData(geoJson);
                    }else{
                        _map.addSource(Env.source.equipment, {
                            type : "geojson",
                            data : geoJson
                        });
                    }
                })
                .catch((err)=>{
                    console.error(err);
                    // alert("An error occurred while retrieving facility information. Please contact the administrator");
                })
        }
        
        toggleEquipmentLayer = function(typeCode) {
            _core.toggleLayer(Env.layer.equipment+"_"+typeCode);
        }
        
        drawEquipment = function(typeCode){
            const _this = this;
            _core.drawIcon(['get','CD_ID'],Env.source.equipment,Env.layer.equipment+"_"+typeCode, ['==','CD_ID',typeCode], function(...layerObj){
                for(const obj of layerObj){
                    _map.off("mouseenter",obj.id,_this.hoverEvent);
                    _map.off("mouseleave",obj.id,_this.leaveEvent);
                    _map.on("mouseenter",obj.id,_this.hoverEvent);
                    _map.on("mouseleave",obj.id,_this.leaveEvent);
                }
            });
        }
        hoverEvent(e) {
            _map.getCanvas().style.cursor = 'pointer';

            // Copy coordinates array.
            const coordinates = e.features[0].geometry.coordinates.slice();
            const prop = e.features[0].properties;
            

            const html = `
                <div class="mkPopWrap">
                    <div class="mkCon">
                        <p class="mkTitle">Type</p>
                        <p>${prop.CD_NM}</p>
                    </div>
                    <div class="mkCon">
                        <p class="mkTitle">Name</p>
                        <p>${prop.EQP_NM}</p>
                    </div>
                    <div class="mkCon">
                        <p class="mkTitle">Road</p>
                        <p>${prop.ROAD_ADDR}</p>
                    </div>
                    <div class="mkCon">
                        <p class="mkTitle">Status</p>
                        <p>${prop.USE_YN === 'Y' ? 'Activate' : 'Deactivate'}</p>
                    </div>
                </div>
            `
            while (Math.abs(e.lngLat.lng - coordinates[0]) > 180) {
                coordinates[0] += e.lngLat.lng > coordinates[0] ? 360 : -360;
            }

            // Populate the popup and set its coordinates
            // based on the feature found.
            popup.setLngLat(coordinates).setHTML(html).addTo(_map);
        }
        leaveEvent() {
            _map.getCanvas().style.cursor = '';
            popup.remove();
        }
    }
    
    return _core;
};