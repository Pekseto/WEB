Vue.component("register", { 
	data: function () {
	    return { 
			customer:{
				id:0,
				username: null,
				password: null,
				firstName: null,
				lastName: null,
	    		gender: null,
	    		role: null,
	    		dateOfBirth: null} }
	},
	    template: 
	    `
	    <div class="register-wrapper">
		    <h1 class="register-title">Register</h1>
		    <form method="post" v-on:submit="validate()">
				<div>
					<labe class="labels">Username:</label><br>
					<input class="text_input" name="username" type="text" v-model="customer.username">
				</div>
				<div>
					<label class="labels">Password:</label><br>
					<input class="text_input" name="password" type="password" v-model="customer.password">
				</div>				
				<div>
					<label class="labels">Confirm password:</label><br>
					<input class="text_input" name="confirmPassword" type="password">
				</div>
				<div>
					<label class="labels">First name:</label><br>
					<input class="text_input" name="firstName" type="text" v-model="customer.firstName">
				</div>
				<div>
					<label class="labels">Last name:</label><br>
					<input class="text_input" name="lastName" type="text" v-model="customer.lastName">
				</div>
				<div>
					<label class="labels">Gender:</label><br>
					<select class="text_input" name="gender" v-model="customer.gender">
						<option value="Male">Male</option>
						<option value="Female">Female</option>
					</select>
				</div>
				<div>
					<label class="labels">Date of birth:</label><br>
					<input class="text_input" name="dateOfBirth" type="date" v-model="customer.dateOfBirth">
				</div>
		    	<button class="btn" type="submit">Register</button>
		    </form>
		</div>
	    `,
    mounted () {
        
    },
    methods: {
    	validate : function() {
			event.preventDefault();
			let success =  true;
			
			let username = document.getElementsByName("username")[0];
			if(!username.value){
				username.style.background = "red";
				success = false;
			}
			let password = document.getElementsByName("password")[0];
			if(!password.value){
				password.style.background = "red";
				success = false;
			}
			let confirmPassword = document.getElementsByName("confirmPassword")[0];
			if(!confirmPassword.value){
				confirmPassword.style.background = "red";
				success = false;
			}
			if(password.value != confirmPassword.value){
				password.style.background = "red";
                confirmPassword.style.background = "red";
                success = false;
			}
			let firstName = document.getElementsByName("firstName")[0];
			if(!firstName.value){
				firstName.style.background = "red";
				success = false;
			}
			let lastName = document.getElementsByName("lastName")[0];
			if(!lastName.value){
				lastName.style.background = "red";
				success = false;
			}
			let gender = document.getElementsByName("gender")[0];
			if(!gender.value){
				gender.style.background = "red";
				success = false;
			}
			let dateOfBirth = document.getElementsByName("dateOfBirth")[0];
			if(!dateOfBirth.value){
				dateOfBirth.style.background = "red";
				success = false;
			}
			
			if(success){
				axios.post('rest/users/register/', this.customer)
				.then((response) => {
					console.log("User registered successfully: ", response.data);
					router.push('/login');
				})
				.catch((error) => console.log(error.response));
			return success;
			}
    	}
    }
});

var link = document.createElement("link");
link.rel = "stylesheet";
link.type = "text/css";
link.href = "css/register.css";
var firstLink = document.getElementsByTagName("link")[0];
document.head.insertBefore(link, firstLink);