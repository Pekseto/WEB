Vue.component("home-page", {
    data: function () {
        return {
            filterParams:{
                avgRating: "",
                fuelFilter: "",
                gearshiftFilter: "",
                onlyOpen: false,
            },
            searchParams:{
                location: "",
                shop: "",
                vehicleType: "",
                rating: "",
            },
            objects: null,
            locations: null,         
            user: null,         
            sortColumn: null,
            sortFilter: null,
        }
    },

    template:
        `
        <div class="container">
        <div class="navbar">
          <button class="navbar-button" type="button" v-if="user == null" v-on:click="register()">Register</button>
          <button class="navbar-button" type="button" v-if="user == null" v-on:click="login()">Log in</button>
          <button class="navbar-button" type="button" v-if="user != null" v-on:click="profile()">Profile</button>
          <button class="navbar-button" type="button" v-if="user != null && user.role == 'Customer'"
            v-on:click="cart()">Cart</button>
          <button class="navbar-button" type="button" v-if="user != null && user.role == 'Administrator'"
            v-on:click="registeredUsers()">Registered users</button>
          <button class="navbar-button" type="button" v-if="user != null && user.role == 'Administrator'"
            v-on:click="registerNewObject()">Register new object</button>
        </div>
      
        <div class="top-container">
          <form class="search-wrapper" v-on:submit="search()">
            <input class="search_input" type="text" id="searchInput" placeholder="Location" v-model="searchParams.location">
            <input class="search_input" type="text" id="searchInput" placeholder="Shop" v-model="searchParams.shop">
            <select class="search_input" v-model="searchParams.vehicleType">
              <option value="Car">Car</option>
              <option value="Van">Van</option>
              <option value="MobileHome">Mobile home</option>
            </select>
            <select class="search_input" id="avgRating" v-model="searchParams.rating">
              <option value="1">1</option>
              <option value="2">2</option>
              <option value="3">3</option>
              <option value="4">4</option>
              <option value="5">5</option>
            </select>
            <button class="btnn btn-search" type="submit">Search</button>
            <button class="btnn btn-search" type=button v-on:click="resetAll()">Reset search</button>
          </form>
          <div class="filter-wrapper">
            <select class="search_input" id="gearshiftFilter" v-model="filterParams.gearshiftFilter">
              <option value="Automatic">Automatic</option>
              <option value="Manual">Manual</option>
            </select>
            <select class="search_input" id="fuelFilter" v-model="filterParams.fuelFilter">
              <option value="Diesel">Diesel</option>
              <option value="Gasoline">Gasoline</option>
              <option value="Electric">Electric</option>
              <option value="Hybrid">Hybrid</option>
            </select>
            <select class="search_input" id="avgRating" v-model="filterParams.avgRating">
              <option value="1">1</option>
              <option value="2">2</option>
              <option value="3">3</option>
              <option value="4">4</option>
              <option value="5">5</option>
            </select>
            <input type="checkbox" id="onlyOpen" v-model="filterParams.onlyOpen">
            <label>Only open</label>
            <div>
              <button class="btnn btn-search" style="margin-left:23rem;" type="button"
                v-on:click="filterShop()">Filter</button>
              <button class="btnn btn-search" type="button" v-on:click="filterReset()">Reset filter</button>
            </div>
          </div>
        </div>
      
      
        <div class="main-div">
          <select class="search_input" id="sortColumn" v-model="sortColumn">
            <option value="Name">Name</option>
            <option value="Location">Location</option>
            <option value="AverageRating">Rating</option>
          </select>
          <select class="search_input" id="sortFilter" v-model="sortFilter">
            <option value="Asc">Asc</option>
            <option value="Desc">Desc</option>
          </select>
          <button class="btnn btn-search" type="button" v-on:click="sort()">Sort</button>
          <div class="card-container" v>
            <table>
              <tr>
                <th style="width:200px"></th>
                <th>Name</th>
                <th>Street</th>
                <th>City</th>
                <th>Rating</th>
                <th></th>
              </tr>
              <tr v-for="object in objects">
                <td style="width:200px"><img :src="object.logo" alt="Logo" class="logo-image" style="max-width: 30%;"></td>
                <td>{{object.name}}</td>
                <td>{{object.location.street}}</td>
                <td>{{object.location.city}}</td>
                <td>{{object.rating}}</td>
                <td><button class="btnn" type="button" v-on:click="goToShop(object.id)">View shop</button></td>
              </tr>
            </table>
          </div>
        </div>
      </div>
    `,

    mounted() {
        axios.get('rest/locations/getAll')
            .then(response => {
                this.locations = response.data;
            });
        axios.get('rest/rentACarObjects/getAllForHomePage')
            .then(response => {
                this.objects = response.data;
                console.log(response.data);
            });
        axios.get('rest/users/getByUsername/' + this.getCookie("username"))
            .then(response => {
                this.user = response.data;
            });
    },

    methods: {
        login: function () {
            router.push('/login');
        },

        register: function () {
            router.push('/register');
        },

        profile: function () {
            router.push('/profileOverview');
        },

        goToShop: function (id) {
            router.push('/showRentACarObject/' + id);
        },

        cart: function () {
            router.push('/showCart');
        },

        registeredUsers: function(){
            router.push('/showUsers');
        },

        registerNewObject: function(){
            router.push('/createRentACarObject');
        },

        sort: function () {
            axios.get('rest/rentACarObjects/sort/' + this.sortColumn + '/' + this.sortFilter)
                .then(response => {
                    this.objects = response.data;
                })
        },

        filterReset: function () {
            this.fuelFilter = null;
            this.gearshiftFilter = null;
            this.onlyOpen = false;
            axios.get('rest/rentACarObjects/search/' + this.searchFilter + '/' + this.searchInput)
                .then(response => {
                    this.objects = response.data;
                });
        },

        filterShop: function () {
            axios.post('rest/rentACarObjects/filter/', this.filterParams)
                .then(response => {
                    this.objects = response.data;
                });
        },

        search: function () {
            event.preventDefault();
            axios.post('rest/rentACarObjects/search/', this.searchParams)
                .then(response => {
                    this.objects = response.data;
                });
        },

        resetAll: function() {
            this.searchFilter = null;
            this.searchInput = null;
            axios.get('rest/rentACarObjects/getAllForHomePage')
                .then(resposne => {
                    this.objects = resposne.data;
                })
        },

        getCookie: function (cname) {
            let name = cname + "=";
            let decodedCookie = decodeURIComponent(document.cookie);
            let ca = decodedCookie.split(';');
            for (let i = 0; i < ca.length; i++) {
                let c = ca[i];
                while (c.charAt(0) == ' ') {
                    c = c.substring(1);
                }
                if (c.indexOf(name) == 0) {
                    return c.substring(name.length, c.length);
                }
            }
            return "";
        },
    }
})

var link = document.createElement("link");
link.rel = "stylesheet";
link.type = "text/css";
link.href = "css/homePage.css";
var firstLink = document.getElementsByTagName("link")[0];
document.head.insertBefore(link, firstLink);
