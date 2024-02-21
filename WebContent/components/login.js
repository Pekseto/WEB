Vue.component("login", {
	data: function() {
		return {
			username: null,
			password: null,
			isFieldValid: {
				username: true,
				password: true,
			},
			isBlocked: false,
		}
	},

	template:
	`
		<div class="login-wrapper">
			<h1 class="login-title">Login</h1>
			<form method="post" v-on:submit="login()">
				<div>
					<label class="labels">Username:</label><br>
					<input name="username" type="text" v-model="username" class="text_input">
					<span v-if="!isFieldValid.username">This field can not be empty!</span>
				</div>
				<div>
					<label class="labels">Password:</label><br>
					<input name="password" type="password" v-model="password" class="text_input">
					<span v-if="!isFieldValid.password">This field can not be empty!</span>
				</div>
				<button type="submit" class="btn">Log in</button>
			</form>
		</div>
    `,

	mounted() {
		document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    },

	methods: {
		login: function() {
			event.preventDefault();
			let valid = true;

			let username = document.getElementsByName("username")[0];
			if (!username.value) {
				isFieldValid.username = false;
				valid = false;
			}
			let password = document.getElementsByName("password")[0];
			if (!password.value) {
				isFieldValid.password = false;
				valid = false;
			}

			if (valid) {

				axios.get('rest/users/checkBlockStatus/' + this.username)
					.then(response => {
						if (!response.data) {
							console.log("User is blocked");
							alert("This user is blocked!")// this.$toasted.show("This user is blocked");
						}

						else {
							axios.get('rest/users/login/' + this.username + '/' + this.password)
								.then(response => {
									if (response.data) {
										this.setCookie("username", this.username, 365);
										console.log("Login success!", response.data);
										router.push("/");
									}
									else {
										alert("Wrong username or password"); //Change in toast
									}
								})
						}
					})
			}
		},

		setCookie: function(cname, cvalue, exdays) {
			const d = new Date();
			d.setTime(d.getTime() + (exdays*24*60*60*1000));
			let expires = "expires="+ d.toUTCString();
			document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
		  },
	}
});

var link = document.createElement("link");
link.rel = "stylesheet";
link.type = "text/css";
link.href = "css/login.css";
var firstLink = document.getElementsByTagName("link")[0];
document.head.insertBefore(link, firstLink);