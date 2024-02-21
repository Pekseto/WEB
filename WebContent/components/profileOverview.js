Vue.component("profile-overview", {
	data: function () {
		return {
			user: {
				id: 0,
				username: null,
				password: null,
				firstName: null,
				lastName: null,
				gender: null,
				role: null,
				dateOfBirth: null
			},
			orders: null,
			priceFrom: null,
			priceTo: null,
			dateFrom: null,
			dateTo: null,
			shopName: null,
			sortColumn: null,
			sortType: null,
			seeAllVehicles: false,
			vehicles: null
		}
	},
	template:
		`
	<div>
	<h1>Profile</h1>
		<div class="profile-info-wrapper">
			<div class="profile-info">
				<label class="profile-data-labels">Username: </label>
				<label class="profile-data-labels">{{user.username}}</label><br>
				<label class="profile-data-labels">First name: </label>
				<label class="profile-data-labels">{{user.firstName}}</label><br>
				<label class="profile-data-labels">Last name: </label>
				<label class="profile-data-labels">{{user.lastName}}</label><br>
				<label class="profile-data-labels">Gender: </label>
				<label class="profile-data-labels">{{user.gender}}</label><br>
				<label class="profile-data-labels">Role: </label>
				<label class="profile-data-labels">{{user.role}}</label><br>
				<label class="profile-data-labels">Date of birth: </label>
				<label class="profile-data-labels">{{user.dateOfBirth}}</label><br>
				<button class="btnn btn-search" type="button" v-on:click="editProfileData()">Edit information</button>
			</div>
		</div>
		<div>
			<div class="top-container"  v-if="user.role == 'Customer' || user.role == 'Manager'">
				<div>
					<input class="search_input inputv2" v-if="user.role == 'Customer'" type=text id="shopName" v-model="shopName" placeholder="Shop name">
					<input class="search_input inputv2" type=number id="priceFrom" v-model="priceFrom" placeholder="Price from">
					<input class="search_input inputv2" type=number id="priceTo" v-model="priceTo" placeholder="Price to">
					<input class="search_input inputv2" type=date id="dateFrom" v-model="dateFrom" placeholder="Date from">
					<input class="search_input inputv2" type=date id="dateFrom" v-model="dateTo" placeholder="Date to">
					<button class="btnn btn-search" type="button" v-on:click="searchOrders()">Search</button>
					<button class="btnn btn-search" type="button" v-on:click="resetSearch()">Reset search</button>
				</div>

				<div>
					<select class="search_input inputv2" v-model="sortColumn">
						<option value="price">Price</option>
						<option value="date">Date</option>
						<option value="shopName" v-if="user.role == 'Customer'">Shop name</option>
					</select>
					<select class="search_input inputv2" v-model="sortType">
						<option value="asc">Asc</option>
						<option value="desc">Desc</option>
					</select>
					<button class="btnn btn-search" type="button" v-on:click="sort()">Sort</button>
				</div>
			</div>
			<table v-if="user.role == 'Customer'">
				<tr>
					<th>Id<th>
					<th>Rent a car</th>
					<th>Time</th>
					<th>Duration</th>
					<th>Status</th>
					<th>Price</th>
					<th>Vehicles</th>
					<th></th>
				</tr>
				<tr v-for="order in orders">
					<td>{{order.id}}</td>
					<td>{{order.rentACarObject.name}}</td>
					<td>{{order.beginDate.year}}/{{order.beginDate.monthValue}}/{{order.beginDate.dayOfMonth}}</td>
					<td>{{order.rentDuration}}</td>
					<td>{{order.status}}</td>
					<td>{{order.price}}</td>
					<td>
						<button class="btnn" v-on:click="seeVehicles(order.id)">Vehicles</button>
						</td>
					<td>
						<button class="btnn" v-if="order.status == 'Processing'" v-on:click="cancelOrder(order)">Cancel</button>		
					</td>
				</tr>
			</table>
			<table v-if="user.role == 'Manager'">
				<tr>
					<td>Id</td>
					<td>Rent a car</td>
					<td>Time</td>
					<td>Duration</td>
					<td>Status</td>
					<td>Price</td>
					<td>Customer name</td>
					<td>Customer last name</td>
					<td style="border-left: 2px solid black"></td>
					<td></td>
				</tr>
				<tr v-for="order in orders">
					<td>{{order.id}}</td>
					<td>{{order.rentACarObject.name}}</td>
					<td>{{order.beginDate.year}}/{{order.beginDate.monthValue}}/{{order.beginDate.dayOfMonth}}</td>
					<td>{{order.rentDuration}}</td>
					<td>{{order.status}}</td>
					<td>{{order.price}}</td>
					<td>{{order.user.firstName}}</td>
					<td>{{order.user.lastName}}</td>
					<td style="border-left: 2px solid black">
						<button class="btnn" v-on:click="seeVehicles(order.id)">Vehicles</button>
					</td>
					<td>
						<button class="btnn" v-if="order.status == 'Processing'" v-on:click="approveOrder(order)">Approve</button>
						<button class="btnn" v-if="order.status == 'Processing'" v-on:click="rejectOrder(order)">Reject</button>
						<button class="btnn" v-if="order.status == 'Approved'" v-on:click="retrieveOrder(order)">Retrieve</button>
						<button class="btnn" v-if="order.status == 'Retrieved'" v-on:click="returnOrder(order)">Return</button>
					</td>
				</tr>
			</table>
			<div v-if="seeAllVehicles" class="popup-overview">
				<table>
					<tr>
						<td>Brand</td>
						<td>Model</td>
						<td>Price</td>
						<td>Type</td>
						<td>Kind</td>
					</tr>
					<tr v-for="vehicle in vehicles">
						<td>{{vehicle.brand}}</td>
						<td>{{vehicle.model}}</td>
						<td>{{vehicle.price}}</td>
						<td>{{vehicle.type}}</td>
						<td>{{vehicle.kind}}</td>
					</tr>
				</table>
				<button class="btn btn-popup" type="button" v-on:click="closePopup()">Close</button>
			</div>
		</div>
	</div>
	`
	,
	mounted() {

		axios.get('rest/users/getByUsername/' + this.getCookie("username"))
			.then(response => {
				this.user = response.data;
			})

		axios.get('rest/orders/getAllOrders/' + this.getCookie("username"))
			.then(response => {
				this.orders = response.data;
				console.log(this.orders);
			});
	},
	methods: {
		closePopup: function () {
			this.seeAllVehicles = !this.seeAllVehicles;
		},

		editProfileData: function () {
			router.push('/editProfileData');
		},

		seeVehicles: function (id) {
			axios.get('rest/vehicles/getAllForOrder/' + id)
				.then(response => {
					this.vehicles = response.data;
				})
			this.seeAllVehicles = !this.seeAllVehicles;
		},

		searchOrders: function () {
			if (this.user.role == "Customer") {
				axios.get('rest/orders/getAllBySearchForCustomer/' + this.getCookie("username") + '/' + this.priceFrom + '/' + this.priceTo + '/' + this.dateFrom + '/' + this.dateTo + '/' + this.shopName)
					.then(response => {
						this.orders = response.data;
					})
			}
			else if (this.user.role == "Manager") {
				axios.get('/' + this.getCookie("username"))
					.then(response => {
						this.orders = response.data;
					})
			}
		},


		resetSearch: function () {
			axios.get('rest/orders/getAllOrders/' + this.getCookie("username"))
				.then(response => {
					if (this.user.role == "Customer")
						this.orders = response.data;
					else (this.user.role == "Manager")
					this.orders = response.data;
				})
		},

		sort: function () {
			axios.get('rest/orders/sortForManager/' + this.sortColumn + '/' + this.sortType)
				.then(response => {
					this.orders = response.data;
				})

		},

		cancelOrder: function (orderForCancel) {
			axios.post('rest/orders/cancelOrder/' + orderForCancel.id + '/' + orderForCancel.customerId + '/' + orderForCancel.price)
				.then(response => {
					axios.get('rest/orders/getAllOrders/' + this.getCookie("username"))
						.then(response => {
							this.orders = response.data;
						});
				})
		},

		approveOrder: function (orderForApprove) {
			axios.post('rest/orders/approveOrder/' + orderForApprove.id)
				.then(response => {
					axios.get('rest/orders/getAllOrders/' + this.getCookie("username"))
						.then(response => {
							this.orders = response.data;
						});
				})
		},

		rejectOrder: function (orderForReject) {
			axios.post('rest/orders/rejectOrder/' + orderForReject.id)
				.then(response => {
					axios.get('rest/orders/getAllOrders/' + this.getCookie("username"))
						.then(response => {
							this.orders = response.data;
						});
				})
		},

		retrieveOrder: function (orderForRetrieve) {
			axios.post('rest/orders/retrieveOrder/' + orderForRetrieve.id)
				.then(response => {
					if (!response.data) {
						alert(`You can only retrieve the order between ${orderForRetrieve.beginDate.year}/${orderForRetrieve.beginDate.monthValue}/${orderForRetrieve.beginDate.dayOfMonth} and ${orderForRetrieve.endDate.year}/${orderForRetrieve.endDate.monthValue}/${orderForRetrieve.endDate.dayOfMonth}!`);
						return;
					}
					axios.get('rest/orders/getAllOrders/' + this.getCookie("username"))
						.then(response => {
							this.orders = response.data;
						});
				})
		},

		returnOrder: function (orderForReturn) {
			axios.post('rest/orders/returnOrder/' + orderForReturn.id)
				.then(response => {
					if (!response.data) {
						alert(`Error while returning order`);
						return;
					}
					axios.get('rest/orders/getAllOrders/' + this.getCookie("username"))
						.then(response => {
							this.orders = response.data;
						});
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
		}
	}
});

var link = document.createElement("link");
link.rel = "stylesheet";
link.type = "text/css";
link.href = "css/profileOverview.css";
var firstLink = document.getElementsByTagName("link")[0];
document.head.insertBefore(link, firstLink);