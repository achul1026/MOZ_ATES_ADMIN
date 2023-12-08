require([
        "esri/config",
        "esri/Map",
        "esri/PopupTemplate",
        "esri/views/MapView",
        "esri/Graphic",
        "esri/geometry/Point"
      ], (esriConfig, Map, PopupTemplate, MapView, Graphic, Point) => {
        esriConfig.apiKey = "AAPK19c36f2188c44aa2b033d5206bf35844z_92dWyvCtywOPdZB3x9gy4549eGEEsu3B0CB_ay6CBO1Ssr1orhf-PTBRX6CI00";
        
        
		var lat = $("#enflat").text();
		var lng = $("#enflng").text();
		var lat2 = $("#acdntlat").text();
		var lng2 = $("#acdntlng").text();
        if(lat==""){
        	lat = lat2
        	lng = lng2
        }
        const map = new Map({
          basemap: "gray-vector"
        });

    const view = new MapView({
        container: "map",
        center: [lng, lat], // longitude,latitude
        zoom: 16, // Zoom level
        map: map,
        navigation : {
			mouseWheelZoomEnabled: true
			}
   		 });
   	view.ui.empty("top-left"); //확장,축소버튼 제거
  
        const point1 = new Point({
	   	  latitude: lat, 
		  longitude: lng,
        });


        
        const textSymbol = {
          type: "text", 
          color: "#FF0000",
          text: "", 
          font: {
            size: 36,
            family: "CalciteWebCoreIcons"
          }
        };

        const pointGraphic1 = new Graphic({
          geometry: point1,
          symbol: textSymbol
        });

        view.graphics.addMany([pointGraphic1
        ]);
});


      	