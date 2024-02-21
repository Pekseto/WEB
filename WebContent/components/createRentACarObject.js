Vue.component("create-rentacar-object", {
	data: function () {
		return {
			rentACarObject: {
				name: null,
				workingHours: null,
				logo: null,
				locationId: null,
			},
			location: {
				street: null,
				city: null,
				postalCode: null,
				longitude: null,
				latitude: null,
			},
			manager: {
				username: null,
				password: null,
				firstName: null,
				lastName: null,
				gender: null,
				role: null,
				dateOfBirth: null,
			},
			isFieldValid: {
				nameTaken: true,
				name: true,
				workingHours: true,
				logo: true,
				street: true,
				city: true,
				postalCode: true,
				longitude: true,
				latitude: true,
				managerId: true,
				username: true,
				password: true,
				confirmPassword: true,
				firstName: true,
				lastName: true,
				gender: true,
				dateOfBirth: true,
			},
			managers: null,
			managerId: null,
			names: null,
			objectData: null,
			doRegisterManager: false,

			mapInteraction: null,
		};
	},

	template:
		`
	<form class="login-wrapper" action="post" v-on:submit="validate()">
		  <h2 class="login-title" style="padding-top: 0">Create Rent A Car object</h2>
		  <div>
			  <label class="labels">Name:</label><br>
			  <input class="text_input" name="name" type="text" v-model="rentACarObject.name"><br>
			  <span v-if="!isFieldValid.nameTaken">This name is already taken! Please enter another one.</span>
			  <span v-if="!isFieldValid.name">This field can not be empty!</span>
		  </div>
		  <div>
			  <label class="labels">Address (Street and number):</label><br>
			  <input class="text_input" name="street" type="text" v-model="location.street"><br>
			  <span v-if="!isFieldValid.street">This field can not be empty!</span>	
		  </div>
		  <div>
			  <label class="labels">City and Postal code:</label><br>
			  <input class="text_input" name="city" type="text" v-model="location.city"><br>
			  <input class="text_input" name="postalCode" type="number" v-model="location.postalCode"><br>
			  <span v-if="!isFieldValid.city">This field can not be empty!</span>
			  <span v-if="!isFieldValid.postalCode">This field can not be empty!</span>	
		  </div>
		  <div>
			  <label class="labels">Location (Longitude and Latitude):</label><br>
			  <input class="text_input" name="longitude" type="number" v-model="location.longitude"><br>
			  <input class="text_input" name="latitude" type="number" v-model="location.latitude"><br>
			  <span v-if="!isFieldValid.longitude">This field can not be empty!</span>
			  <span v-if="!isFieldValid.latitude">This field can not be empty!</span>
			  <div style="width: 100%; height: 400px;" id="map"></div>
		  </div>
		  <div>
			  <label class="labels">Working hours:</label><br>
			  <input class="text_input" name="workingHours" type="text" v-model="rentACarObject.workingHours"><br>
			  <span v-if="!isFieldValid.workingHours">This field can not be empty!</span>	
		  </div>
		  <div>
			  <label class="labels">Logo(url):</label><br>
			  <input class="text_input" type="text" name="logo" v-model="rentACarObject.logo"><br>
			  <span v-if="!isFieldValid.logo">This field can not be empty!</span>
		  </div>
		  <div>		
			  <img v-if="rentACarObject.logo" :src="rentACarObject.logo" alt="Selected Image" style="max-width: 30%;">		
		  </div>
		  <div>
			  <label class="labels">Available managers:</label><br>
			  <select class="text_input" name="manager" v-model="managerId">
				<option v-for="manager in managers" :value="manager.id">{{manager.username}}</option>
			  </select>
			  <button style="background-color:skyblue; color:white;" type="button" id="addManagerButton" v-on:click="registerManagerFunction()">+</button><br>
			  <span v-if="!isFieldValid.managerId">This field can not be empty!</span>
			  <div v-if="doRegisterManager">
				<h3>Register manager</h3>
				<form method="post" v-on:submit="validateManager()">
					<div>
						<label class="labels">Username:</label>
						<input class="text_input" name="username" type="text" v-model="manager.username"><br>
						<span v-if="!isFieldValid.username">This field can not be empty!</span>
					</div>
					<div>
						<label class="labels">Password:</label>
						<input class="text_input" name="password" type="password" v-model="manager.password"><br>
						<span v-if="!isFieldValid.password">This field can not be empty!</span>
					</div>
					<div>
						<label class="labels">Confirm password:</label>
						<input class="text_input" placeholder="HH:MM:SS-HH:MM:SS" name="confirmPassword" type="password"><br>
						<span v-if="!isFieldValid.confirmPassword">This field can not be empty!</span>
					</div>
					<div>
						<label class="labels">First name:</label>
						<input class="text_input" name="firstName" type="text" v-model="manager.firstName"><br>
						<span v-if="!isFieldValid.firstName">This field can not be empty!</span>
					</div>
					<div>
						<label class="labels">Last name:</label>
						<input class="text_input" name="lastName" type="text" v-model="manager.lastName"><br>
						<span v-if="!isFieldValid.lastName">This field can not be empty!</span>
					</div>
					<div>
						<label class="labels">Gender:</label>
						<select class="text_input" name="gender" v-model="manager.gender">
							<option value="Male">Male</option>
							<option value="Female">Female</option>
						</select><br>
						<span v-if="!isFieldValid.gender">This field can not be empty!</span>
					</div>
					<div>
						<label class="labels">Date of birth:</label>
						<input class="text_input" name="dateOfBirth" type="date" v-model="manager.dateOfBirth"><br>
						<span v-if="!isFieldValid.dateOfBirth">This field can not be empty!</span>
					</div>
						<button class="btn" type="submit">Register</button>
				</form>
			  </div>
		  </div><br>
		  <div>
			  <button class="btn" type="submit">Create</button>	
		  </div>
	  </form>
	</div>
  </div>
  `,
	mounted() {
		axios.get('rest/rentACarObjects/names')
			.then(response => {
				this.names = response.data
			});
		axios.get('rest/users/managers')
			.then(response => {
				this.managers = response.data
			});

		var map = new ol.Map({
			target: 'map',
			layers: [
				new ol.layer.Tile({
					source: new ol.source.OSM()
				})
			],
			view: new ol.View({
				center: ol.proj.fromLonLat([this.rentACarObject.longitude, this.rentACarObject.latitude]),
				zoom: 10
			})
		});

		// Dodavanje interakcije za odabir lokacije
		this.mapInteraction = new ol.interaction.Pointer({
			handleDownEvent: function (event) {
				var coordinate = event.coordinate;
				var lonLat = ol.proj.toLonLat(coordinate);

				// Postavljanje vrednosti polja za longitude i latitude
				this.rentACarObject.longitude = lonLat[0].toFixed(4);
				this.rentACarObject.latitude = lonLat[1].toFixed(4);

			}.bind(this)
		});

		// Dodavanje interakcije na mapu
		map.addInteraction(this.mapInteraction);

	},

	beforeDestroy() {
		// Uklanjanje interakcije sa mape
		map.removeInteraction(this.mapInteraction);
	},


	methods: {
		validate: function () {
			event.preventDefault();
			let success = true;

			let name = document.getElementsByName("name")[0];
			if (!name.value) {
				name.style.border = "1px solid red";
				success = false;
				this.isFieldValid.name = false;
			}
			for (let i = 0; i < this.names.length; i++) {
				if (name.value === this.names[i]) {
					name.style.border = "1px solid red";
					success = false;
					this.isFieldValid.nameTaken = false;
				}
			}

			let street = document.getElementsByName("street")[0];
			if (!street.value) {
				street.style.border = "1px solid red";
				success = false;
				this.isFieldValid.street = false;
			}
			let city = document.getElementsByName("city")[0];
			if (!city.value) {
				city.style.border = "1px solid red";
				success = false;
				this.isFieldValid.city = false;
			}
			let postalCode = document.getElementsByName("postalCode")[0];
			if (!postalCode.value) {
				postalCode.style.border = "1px solid red";
				success = false;
				this.isFieldValid.postalCode = false;
			}
			let longitude = document.getElementsByName("longitude")[0];
			if (!longitude.value) {
				longitude.style.border = "1px solid red";
				success = false;
				this.isFieldValid.longitude = false;
			}
			let latitude = document.getElementsByName("latitude")[0];
			if (!latitude.value) {
				latitude.style.border = "1px solid red";
				success = false;
				this.isFieldValid.latitude = false;
			}
			let workingHours = document.getElementsByName("workingHours")[0];
			if (!workingHours.value) {
				workingHours.style.border = "1px solid red";
				success = false;
				this.isFieldValid.workingHours = false;
			}
			let manager = document.getElementsByName("manager")[0];
			if (!manager.value) {
				manager.style.border = "1px solid red";
				success = false;
				this.isFieldValid.managerId = false;
			}
			let logo = document.getElementsByName("logo")[0];
			if (!logo.value) {
				logo.style.border = "1px solid red";
				success = false;
				this.isFieldValid.logo = false;
			}

			if (success) {
				axios.post('rest/locations/create/', this.location)
					.then((response) => {
						this.rentACarObject.locationId = response.data;
						axios.post('rest/rentACarObjects/create/', this.rentACarObject)
							.then((response) => {
								if (!this.rentACarObject.managerId) {
									axios.post('rest/users/' + this.managerId + '/' + response.data);
								}
								router.push('/profileOverview');
							})
					})
					.catch((error) => console.log(error.response));
				return success;
			}
		},

		registerManagerFunction: function () {
			event.preventDefault();
			this.doRegisterManager = !this.doRegisterManager;
			return;
		},

		validateManager: function () {
			event.preventDefault();
			let success = true;

			let username = document.getElementsByName("username")[0];
			if (!username.value) {
				username.style.border = "1px solid red";
				success = false;
				this.isFieldValid.username = false;
			}
			let password = document.getElementsByName("password")[0];
			if (!password.value) {
				password.style.border = "1px solid red";
				success = false;
				this.isFieldValid.password = false;
			}
			let confirmPassword = document.getElementsByName("confirmPassword")[0];
			if (!confirmPassword.value) {
				confirmPassword.style.border = "1px solid red";
				success = false;
				this.isFieldValid.confirmPassword = false;
			}
			if (password.value != confirmPassword.value) {
				password.style.border = "1px solid red";
				confirmPassword.style.border = "1px solid red";
				success = false;
				this.isFieldValid.password = false;
				this.isFieldValid.confirmPassword = false;
			}
			let firstName = document.getElementsByName("firstName")[0];
			if (!firstName.value) {
				firstName.style.border = "1px solid red";
				success = false;
				this.isFieldValid.firstName = false;
			}
			let lastName = document.getElementsByName("lastName")[0];
			if (!lastName.value) {
				lastName.style.border = "1px solid red";
				success = false;
				this.isFieldValid.lastName = false;
			}
			let gender = document.getElementsByName("gender")[0];
			if (!gender.value) {
				gender.style.border = "1px solid red";
				success = false;
				this.isFieldValid.gender = false;
			}
			let dateOfBirth = document.getElementsByName("dateOfBirth")[0];
			if (!dateOfBirth.value) {
				dateOfBirth.style.border = "1px solid red";
				success = false;
				this.isFieldValid.dateOfBirth = false;
			}

			if (success) {
				axios.post('rest/users/registerManager/', this.manager)
					.then((response) => {
						console.log("Manager registered successfully: ", response.data);
						axios.get('rest/users/managers')
							.then(response => {
								this.managers = response.data
							});
					})
					.catch((error) => console.log(error.response));
				return success;
			}
		},

	},
});

var link = document.createElement("link");
link.rel = "stylesheet";
link.type = "text/css";
link.href = "css/createRentACarObject.css";
var firstLink = document.getElementsByTagName("link")[0];
document.head.insertBefore(link, firstLink);