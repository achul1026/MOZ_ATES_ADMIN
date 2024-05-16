class DriverSelectAPI {
	constructor() {
		
    }
	
    async fetchData(searchType, searchValue, callback) {
        let apiUrl = '';
        let data = {};

        switch (searchType) {
            case 'dvrLcenId':
                apiUrl = '/common/api/searchDriver';
                data = { 
					"searchType" : searchType,
					"searchValue" : searchValue
					};
                break;
            case 'vehicleNo':
                apiUrl = '/api/searchVehicleNo';
                data = { 
					"searchType" : searchType,
					"searchValue" : searchValue
					};
                break;
            case 'driverNm':
                apiUrl = '/common/api/searchDriver';
                data = { 
					"searchType" : searchType,
					"searchValue" : searchValue
					};
                break;
            default:
                console.error('Invalid search type');
                return;
        }

        fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(data),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => callback(data))
        .catch(error => 
        	console.error('Error:', error)
        );
    }
}