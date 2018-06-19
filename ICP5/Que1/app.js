angular.module('myApp', [])

    .controller('View1Ctrl', function ($scope, $http) {
        $scope.mostRecentReview;
        $scope.getVenues = function () {
            $scope.venueList = [];
            $("#error").hide();
            var placeEntered = $scope.place;
            var searchQuery = $scope.searchQ;
            if (placeEntered != null && placeEntered != "" && searchQuery != null && searchQuery != "") {
                $("#helpBlock").hide();
                //This is the API that gives the list of venues based on the place and search query.
                var handler = $http.get("https://api.foursquare.com/v2/venues/search" +
                    "?client_id=5DHND4BAC353XFUM0TFBBF1JQRET3L4G3BEZMFQKX4TMKQH1" +
                    "&client_secret=4WLZKTNJRV0VBG0QU1FPL4J0ZTGXZZ0NY4Z5L2CTDEDJZIQF" +
                    "&v=20160215&limit=5" +
                    "&near=" + placeEntered +
                    "&query=" + searchQuery);

                handler.success(function (data) {
                    if (data != null && data.response != null && data.response.venues != undefined && data.response.venues != null) {
                        $("#helpBlock").show();
                        // Tie an array named "venueList" to the scope which is an array of objects.
                        // Each object should have key value pairs where the keys are "name", "id" , "location" and values are their corresponding values from the response
                        // Marks will be distributed between logic, implementation and UI
                        data.response.venues.forEach(function (value) {
                            var location = value.location.formattedAddress;
                            if(location != null){
                                location = location.toString();
                            };
                            var review = "";
                            $http.get("https://api.foursquare.com/v2/venues/"+value.id+"/tips?" +
                                "client_id=5DHND4BAC353XFUM0TFBBF1JQRET3L4G3BEZMFQKX4TMKQH1" +
                                "&client_secret=4WLZKTNJRV0VBG0QU1FPL4J0ZTGXZZ0NY4Z5L2CTDEDJZIQF").success(function (data) {
                                review = data.response.tips.items[0].text;
                                $scope.venueList.push({name : value.name, id : value.id, location : location, review: review})
                            });
                        });
                    }else{
                        $("#error").text("No Venues present for the Input values");
                        $("#error").show();
                    }
                })
                handler.error(function (data) {
                    alert("There was some error processing your request. Please try after some time.");
                });
            }else{
                $("#error").show();
            }
        }
    });
