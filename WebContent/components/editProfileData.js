Vue.component("edit-profile", {
    data: function () {
        return {
            user: {
                id:0,
				username: null,
				password: null,
				firstName: null,
				lastName: null,
	    		gender: null,
	    		role: null,
	    		dateOfBirth: null
            },
            isValid: {
                username: true,
                password: true,
                confirmPassword: true,
                firstName: true,
                lastName: true,
                gender: true,
                role: true,
                dateOfBirth: true
            },
            cookieUser: null,
        }
    },

    template:
    `
    <div class="login-wrapper">
        <h1 class="login-title">Edit data</h1>
        <div>
            <label class="labels">Username: </label><br>
            <input class="text_input" type=text name="username" v-model="user.username"><br>
            <span v-if="!isValid.username">Field cant be empty<br></span>
        </div>
        <div>
            <label class="labels">First name: </label><br>
            <input class="text_input" type=text name="firstName" v-model="user.firstName"><br>
            <span v-if="!isValid.firstName">Field cant be empty<br></span>
        </div>
        <div>
            <label class="labels">Last name: </label><br>
            <input class="text_input" type=text name="lastName" v-model="user.lastName"><br>
            <span v-if="!isValid.lastName">Field cant be empty<br></span>
        </div>
        <div>
            <label class="labels">Password: </label><br>
            <input class="text_input" type=password name="password" v-model="user.password"><br>
            <span v-if="!isValid.password">Field cant be empty<br></span>
        </div>
        <div>
            <label class="labels">Confirm password: </label><br>
            <input class="text_input" type=password name="confirmPassword" v-model="user.confirmPassword"><br>
        </div>
        <div>
            <label class="labels">Gender: </label><br>
            <select class="text_input" name="gender" v-model="user.gender">
                <option value="Male">Male</option>
                <option value="Female">Female</option>
            </select><br>
            <span v-if="!isValid.gender">Field cant be empty<br></span>
        </div>
        <div>
            <label class="labels">Role: </label><br>
            <label class="labels">{{user.role}}</label><br>
        </div>
        <div>
            <label class="labels">Date of Birth: </label><br>
            <input class="text_input" type=date name="dateOfBirth" v-model="user.dateOfBirth"><br>
            <span v-if="!isValid.dateOfBirth">Field cant be empty<br></span>
        </div>
        <button class="btn" type="button" v-on:click="editProfileData()">Edit information</button>
    </div>
    `,

    mounted() {
        axios.get('rest/users/getByUsername/' + this.getCookie("username"))
            .then(response => {
                this.cookieUser = response.data;
                this.user.id = this.cookieUser.id;
                this.user.username = this.cookieUser.username;
                this.user.password = this.cookieUser.password;
                this.user.firstName = this.cookieUser.firstName;
                this.user.lastName = this.cookieUser.lastName;
                this.user.gender = this.cookieUser.gender;
                this.user.role = this.cookieUser.role;
                this.user.dateOfBirth = this.cookieUser.dateOfBirth;
                console.log(this.user);
            })
    },

    methods: {
        editProfileData: function () {
            event.preventDefault();
            let success = true;

            let username = document.getElementsByName("username")[0];
            if (!username.value) {
                username.style.background = "red";
                success = false;
            }
            let password = document.getElementsByName("password")[0];
            if (!password.value) {
                password.style.background = "red";
                success = false;
            }
            let confirmPassword = document.getElementsByName("confirmPassword")[0];
            if (password.value != confirmPassword.value) {
                password.style.background = "red";
                confirmPassword.style.background = "red";
                success = false;
            }
            let firstName = document.getElementsByName("firstName")[0];
            if (!firstName.value) {
                firstName.style.background = "red";
                success = false;
            }
            let lastName = document.getElementsByName("lastName")[0];
            if (!lastName.value) {
                lastName.style.background = "red";
                success = false;
            }
            let gender = document.getElementsByName("gender")[0];
            if (!gender.value) {
                gender.style.background = "red";
                success = false;
            }
            let dateOfBirth = document.getElementsByName("dateOfBirth")[0];
            if (!dateOfBirth.value) {
                dateOfBirth.style.background = "red";
                success = false;
            }

            if (success) {
                axios.post('rest/users/update/' + this.getCookie("username"), this.user)
                    .then(response => {
                        document.cookie = "username=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
                        this.setCookie("username", this.user.username, 365);
                        router.push('/');
                    });
            }

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

        setCookie: function (cname, cvalue, exdays) {
            const d = new Date();
            d.setTime(d.getTime() + (exdays * 24 * 60 * 60 * 1000));
            let expires = "expires=" + d.toUTCString();
            document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
        }
    }
});