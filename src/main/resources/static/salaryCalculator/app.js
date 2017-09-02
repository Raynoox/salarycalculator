var app = angular.module('salaryCalculator', []);
app.controller('salaryCalculatorCtrl', function ($scope, $http) {
    $scope.countries = ['POLAND','GERMANY','UNITED_KINGDOM'];
    $scope.selectedCountry = {};
    $scope.onCalculateClick = function () {
        $http({
            method: "POST",
            url: "/api/salarycalculator",
            data: {
                salary: $scope.dailySalary,
                countryName: $scope.selectedCountry.country
            }
        }).then(onCalculateSuccess, onCalculateFailure);
    };
    function onCalculateSuccess(response) {
        $scope.calculatedResult = response.data;
    }
    function onCalculateFailure(response) {
        alert("Status: "+response.status+" Message: "+response.data.error+"\n"+response.data.message);
    }
    $scope.shouldDisplayResult = function () {
        return !!$scope.calculatedResult;
    };
    $scope.getCountries = function () {
        $http({
            method: "GET",
            url: "/api/countries"
        }).then(function (response) {
            $scope.countries = response.data;
            $scope.selectedCountry = response.data.length > 0 ? response.data[0] : null;
        }, onCalculateFailure);
    }
});