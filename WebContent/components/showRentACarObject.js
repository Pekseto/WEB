Vue.component("show-rentacar-object", {
  data: function () {
    return {
      rentACarObject: {
        id: null,
        name: null,
        workingHours: null,
        isOpen: null,
        logo: null,
        locationId: null,
        rating: null,
      },
      location: {
        street: null,
        city: null,
        postalCode: null,
        longitude: null,
        latitude: null,
      },
      vehicle: {
        id: null,
        brand: null,
        model: null,
        price: null,
        type: null,
        kind: null,
        fuelType: null,
        consumption: null,
        numberOfDoors: null,
        personCapacity: null,
        image: null,
        description: null,
        rentACarObjectId: null,
      },
      date: {
        from: null,
        until: null,
      },
      comment: {
        rating: null,
        text: null,
      },
      objectId: null,
      allVehicles: null,
      foundVehicles: null,
      allComments: null,
      pendingComments: null,
      workWithVehicle: false,
      isAddVehicle: true,
      user: null,
      isFindVehicle: false,
      isShowAddComment: false,
    }
  },

  template:
    `
    <div>
    <h2 class="login-title overview-title">Overview of Rent A Car object</h2>
    <div class="info-wrapper">
      <img :src="this.rentACarObject.logo" alt="Logo" class="logo-image" style="max-width: 15%;">
      <div style="padding-left: 3rem;">
        <div class="info">
          <label class="label-info">Name:</label>
          <label class="label-info"> {{rentACarObject.name}}</label>
        </div>
        <div class="info">
          <label class="label-info">Working hours:</label>
          <label class="label-info">{{rentACarObject.workingHours}}</label>
        </div>
        <div class="info">
          <label class="label-info">Status:</label>
          <label class="label-info" v-if="rentACarObject.status">Open</label>
          <label class="label-info" v-if="!rentACarObject.status">Closed</label>
        </div>
        <div class="info">
          <label class="label-info">Location:</label>
          <label class="label-info">{{location.street}}, {{location.city}}</label>
          <br>
        </div>
        <div class="info">
          <label class="label-info">Rating:</label>
          <label class="label-info">{{rentACarObject.rating}}</label>
          <br>
        </div>
      </div>
    </div>
  
  
    <div id="tableVozila">
      <h2 class="second-title">Vehicles</h2>
      <h4 class="second-title pod-title">List of vehicles</h4>
      <table id="vozilaTable">
        <tr>
          <th>Image</th>
          <th>Brand</th>
          <th>Model</th>
          <th>Price</th>
          <th>Type</th>
          <th>Kind</th>
          <th>Fuel type</th>
          <th>Consumption</th>
          <th>Number of doors</th>
          <th>Person capacity</th>
          <th>Description</th>
          <th></th>
        </tr>
        <tr v-for="v in allVehicles">
        <td>
          <img :src="v.image" alt="Image" class="vehicle-image" style="max-width: 30%;">
        </td>
          <td>{{v.brand}}</td>
          <td>{{v.model}}</td>
          <td>{{v.price}}</td>
          <td>{{v.type}}</td>
          <td>{{v.kind}}</td>
          <td>{{v.fuelType}}</td>
          <td>{{v.consumption}}</td>
          <td>{{v.numberOfDoors}}</td>
          <td>{{v.personCapacity}}</td>
          <td>{{v.description}}</td>
          <td>
            <button class="btnn" v-if="user != null && user.role == 'Manager'" v-on:click="showChangeVehicle(v)">Change</button>
          </td>
        </tr>
      </table>
    </div>
  
    <button class="btn btn-overview" v-if="user != null && user.role == 'Manager'" v-on:click="showAddVehicle()">Add Vehicle</button>
    <button class="btn btn-overview" v-if="user != null && user.role == 'Customer'" v-on:click="showFindVehicle()">Find Vehicle</button>
  
    <div v-if="isFindVehicle">
      <h4 class="third-title">Find vehicle</h4>
      <div>
        <label style="font-size: 1.5rem">Date range:</label>
        <input class="search_input" style="font-size:1rem;" type="date" v-model="date.from" required>
        <input class="search_input" style="font-size:1rem;" type="date" v-model="date.until" required>
        <button class="btnn btn-search" v-on:click="findVehicles()">Find</button>
      </div>
      <table v-if="foundVehicles">
        <tr>
          <th>Brand</th>
          <th>Model</th>
          <th>Price</th>
          <th>Type</th>
          <th>Kind</th>
          <th>Fuel type</th>
          <th>Consumption</th>
          <th>Number of doors</th>
          <th>Person capacity</th>
          <th>Image</th>
          <th>Description</th>
          <th></th>
        </tr>
        <tr v-for="v in foundVehicles">
          <td>{{v.brand}}</td>
          <td>{{v.model}}</td>
          <td>{{v.price}}</td>
          <td>{{v.type}}</td>
          <td>{{v.kind}}</td>
          <td>{{v.fuelType}}</td>
          <td>{{v.consumption}}</td>
          <td>{{v.numberOfDoors}}</td>
          <td>{{v.personCapacity}}</td>
          <td>
            <img :src="v.image" alt="Image" style="max-width: 30%;">
          </td>
          <td>{{v.description}}</td>
          <td>
            <button class="btnn" v-on:click="addToCart(v)">Add to cart</button>
          </td>
        </tr>
      </table>
    </div>
  
    <div v-if="workWithVehicle" class="popup">
      <h2 class="third-title" style="margin-left:21rem" v-if="isAddVehicle">Add a vehicle</h2>
      <h2 class="third-title" style="margin-left:21rem" v-if="!isAddVehicle">Change vehicle</h2>
      <form>
        <div>
          <label class="labels-rent-add">Brand:</label>
          <input class="text-input-rent-add" type="text" id="brand" name="brand" v-model="vehicle.brand" required>
        </div>
        <div>
          <label class="labels-rent-add">Model:</label>
          <input class="text-input-rent-add" type="text" id="model" name="model" v-model="vehicle.model" required>
        </div>
        <div>
          <label class="labels-rent-add">Price:</label>
          <input class="text-input-rent-add" type="number" id="price" v-model="vehicle.price" required>
        </div>
        <div>
          <label class="labels-rent-add">Type:</label>
          <select class="text-input-rent-add" id="type" name="type" v-model="vehicle.type" required>
            <option value="Car">Car</option>
            <option value="Van">Van</option>
            <option value="MobileHome">Mobile Home</option>
          </select>
        </div>
        <div>
          <label class="labels-rent-add">Kind:</label>
          <select class="text-input-rent-add" id="kind" name="kind" v-model="vehicle.kind" required>
            <option value="Manual">Manual</option>
            <option value="Automatic">Automatic</option>
          </select>
        </div>
        <div>
          <label class="labels-rent-add" for="fuelType">Fuel type:</label>
          <select class="text-input-rent-add" id="fuelType" name="fuelType" v-model="vehicle.fuelType" required>
            <option value="Diesel">Diesel</option>
            <option value="Gasoline">Gasoline</option>
            <option value="Hybrid">Hybrid</option>
            <option value="Electric">Electric</option>
          </select>
        </div>
        <div>
          <label class="labels-rent-add">Consumption:</label>
          <input class="text-input-rent-add" type="number" id="consumption" name="consumption" v-model="vehicle.consumption" required>
        </div>
        <div>
          <label class="labels-rent-add" for="numberOfDoors">Number of doors:</label>
          <input class="text-input-rent-add" type="number" id="numberOfDoors" name="numberOfDoors" v-model="vehicle.numberOfDoors" required>
        </div>
        <div>
          <label class="labels-rent-add" for="personCapacity">Number of passengers:</label>
          <input class="text-input-rent-add" type="number" id="personCapacity" name="personCapacity" v-model="vehicle.personCapacity" required>
        </div>
        <div>
          <label class="labels-rent-add" for="image">Image:</label>
          <input class="text-input-rent-add" type="url" id="image" name="image" v-model="vehicle.image" required>
        </div>
        <div>
          <label class="labels-rent-add" for="description">Description:</label>
          <textarea class="text-input-rent-add" id="description" name="description" v-model="vehicle.description" rows="4" cols="50"> </textarea>
        </div>
  
        <div>
          <button class="btn" style="margin-left: 16.5rem" v-if="isAddVehicle" type="submit" id="register-btn" v-on:click="addVehicle()">Add</button>
          <button class="btn" style="margin-left: 16.5rem" v-if="!isAddVehicle" type="submit" id="register-btn" v-on:click="changeVehicle()">Change</button>
        </div>
  
      </form>
    </div>
  
    <div id="tableKomentari">
      <h4 class="third-title">Comments</h4>
      <table id="komentariTable">
        <tr>
          <th>Number</th>
          <th>Text</th>
          <th>Rating</th>
          <th v-if="user != null && (user.role == 'Manager' || user.role == 'Administrator')">Status</th>
        </tr>
        <tr v-for="c in allComments">
          <td>{{c.id}}</td>
          <td>{{c.text}}</td>
          <td>{{c.rating}}</td>
          <td v-if="user != null && (user.role == 'Manager' || user.role == 'Administrator')">{{c.status}}</td>
        </tr>
      </table>
  
      <h4 class="third-title" style="margin-left: 43.5rem"  v-if="user != null && (user.role == 'Manager' || user.role == 'Administrator')">Pending comments</h4>
      <table v-if="user != null && user.role == 'Manager'">
        <tr>
          <th>Number</th>
          <th>Text</th>
          <th>Rating</th>
        </tr>
        <tr v-for="c in pendingComments">
          <td>{{c.id}}</td>
          <td>{{c.text}}</td>
          <td>{{c.rating}}</td>
          <td>
            <button class="btnn btnn-green" v-on:click="approve(c.id)">Approve</button>
          </td>
          <td>
            <button class="btnn btnn-red" v-on:click="deny(c.id)">Deny</button>
          </td>
        </tr>
      </table>
    </div>
  
    <button class="btn btn-overview" v-if="user != null && user.role == 'Customer'" v-on:click="showAddComment()">Add comment</button>
    <div v-if="isShowAddComment">
      <div>
        <label class="labels" style="margin-left:40rem;">Rate (1-5):</label><br>
        <input class="search_input" style="width:20%; margin-left:40rem;" type="number" v-model="comment.rating" required min="1" max="5">
      </div>
      <div>
        <label class="labels" style="margin-left:40rem; margin-top: 3rem;">Comment:</label><br>
        <textarea class="search_input" style="width:20%; margin-left:40rem;" name="comment" id="comment" cols="30" rows="10" placeholder="Enter your comment here"
          v-model="comment.text"></textarea>
      </div>
      <button class="btn btn-overview" type="submit" v-on:click="addComment()">Post comment</button>
    </div>
  
  </div>
	`,
  mounted() {
    this.objectId = this.$route.params.id;
    this.vehicle.rentACarObjectId = this.objectId;
    axios.get('rest/rentACarObjects/getById/' + this.objectId)
      .then(response => {
        this.rentACarObject = response.data;
        axios.get('rest/locations/getById/' + this.rentACarObject.locationId)
          .then(response => {
            this.location = response.data;
            axios.get('rest/vehicles/getAllForObject/' + this.objectId)
              .then(response => {
                console.log(response.data);
                this.allVehicles = response.data;
              })
          })
      })
      .catch((error) => console.log(error.response));

    axios.get('rest/users/getByUsername/' + this.getCookie("username"))
      .then(response => {
        this.user = response.data;
        console.log(this.user);
        if (this.user.role == "Customer") {
          axios.get('rest/comments/getFromObjectForCustomer/' + this.objectId)
            .then(response => {
              console.log(response.data);
              this.allComments = response.data;
            })
        } else {
          axios.get('rest/comments/getAllForObject/' + this.objectId)
            .then(response => {
              console.log(response.data);
              this.allComments = response.data;
            });

          axios.get('rest/comments/getAllPendingForObject/' + this.objectId)
            .then(response => {
              this.pendingComments = response.data;
            })
        }
      })

    if (this.user == null) {
      axios.get('rest/comments/getFromObjectForCustomer/' + this.objectId)
        .then(response => {
          console.log(response.data);
          this.allComments = response.data;
        })
    }

  },

  methods: {
    showAddVehicle: function () {
      event.preventDefault();
      if (this.isAddVehicle) {
        this.workWithVehicle = !this.workWithVehicle;
      }

      this.isAddVehicle = true;
      this.vehicle.brand = null;
      this.vehicle.model = null;
      this.vehicle.price = null;
      this.vehicle.type = null;
      this.vehicle.kind = null;
      this.vehicle.fuelType = null;
      this.vehicle.consumption = null;
      this.vehicle.numberOfDoors = null;
      this.vehicle.personCapacity = null;
      this.vehicle.image = null;
      this.vehicle.description = null;
    },

    addVehicle: function () {
      event.preventDefault();
      console.log(this.vehicle);
      axios.post('rest/vehicles/create/', this.vehicle)
        .then(response => {
          let newVehicle = response.data;
          this.allVehicles.push(newVehicle);

          this.vehicle.brand = null;
          this.vehicle.model = null;
          this.vehicle.price = null;
          this.vehicle.type = null;
          this.vehicle.kind = null;
          this.vehicle.fuelType = null;
          this.vehicle.consumption = null;
          this.vehicle.numberOfDoors = null;
          this.vehicle.personCapacity = null;
          this.vehicle.image = null;
          this.vehicle.description = null;
          this.vehicle.rentACarObjectId = null;

          //this.workWithVehicle = !this.workWithVehicle;
        });
    },

    showChangeVehicle: function (vehicleForChange) {
      event.preventDefault();
      this.isAddVehicle = false;
      this.workWithVehicle = true;
      this.vehicle = Object.assign({}, vehicleForChange)
    },

    showFindVehicle: function () {
      event.preventDefault();
      this.isFindVehicle = !this.isFindVehicle;
    },

    changeVehicle: function () {
      event.preventDefault();
      axios.put('rest/vehicles/change/', this.vehicle)
        .then(response => {
          axios.get('rest/vehicles/getAllForObject/' + this.objectId)
            .then(response => {
              this.allVehicles = response.data;
            })
        })

      this.workWithVehicle = false;
    },

    findVehicles: function () {
      event.preventDefault();
      axios.get('rest/vehicles/getByDateRange/' + this.date.from + '/' + this.date.until + '/' + this.objectId)
        .then(response => {
          if (response.data.length > 0) {
            this.foundVehicles = response.data;
          }
        })
    },

    addToCart: function (vehicleForAdd) {
      event.preventDefault();
      axios.post('rest/carts/addVehicle/' + vehicleForAdd.id + '/' + this.user.id + '/' + this.date.from + '/' + this.date.until)
        .then(response => {
          console.log(response.data);

          for (let i = 0; i < this.foundVehicles.length; i++) {
            if (this.foundVehicles[i].id == vehicleForAdd.id) {
              this.foundVehicles.splice(i, 1);
              break;
            }

          }
        })
    },

    showAddComment: function () {
      event.preventDefault();
      this.isShowAddComment = !this.isShowAddComment;
    },

    addComment: function () {
      event.preventDefault();
      axios.post('rest/comments/create/' + this.objectId + '/' + this.user.id + '/' + this.comment.text + '/' + this.comment.rating)
        .then(response => {
          console.log(response.data);
          if (!response.data) {
            alert("You are not eligible to add a comment!");
          }
        })
    },

    approve: function (commentForApproveId) {
      event.preventDefault();
      axios.post('rest/comments/approveComment/' + commentForApproveId)
        .then(response => {
          for (let i = 0; i < this.pendingComments.length; i++) {
            if (this.pendingComments[i].id == commentForApproveId) {
              this.pendingComments.splice(i, 1);
              break;
            }
          }
          axios.get('rest/comments/getAllForObject/' + this.objectId)
            .then(response => {
              this.allComments = response.data;
            });
          axios.put('rest/rentACarObjects/updateRatingForObject/' + this.objectId);

        })
    },

    deny: function (commentForDenyId) {
      event.preventDefault();
      axios.post('rest/comments/denyComment/' + commentForDenyId)
        .then(response => {
          for (let i = 0; i < this.pendingComments.length; i++) {
            if (this.pendingComments[i].id == commentForDenyId) {
              this.pendingComments.splice(i, 1);
              axios.get('rest/comments/getAllForObject/' + this.objectId)
                .then(response => {
                  console.log(response.data);
                  this.allComments = response.data;
                });
              break;
            }
          }

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
  },

});

var link = document.createElement("link");
link.rel = "stylesheet";
link.type = "text/css";
link.href = "css/showRentACarObject.css";
var firstLink = document.getElementsByTagName("link")[0];
document.head.insertBefore(link, firstLink);
