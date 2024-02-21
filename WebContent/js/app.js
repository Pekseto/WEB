const HomePage = { template: '<home-page></home-page>'}
const Register = { template: '<register></register>' }
const Login = { template: '<login></login>' }
const ProfileOverview = { template: '<profile-overview></profile-overview>' }
const CreateRentACarObject = { template: '<create-rentacar-object></create-rentacar-object>' }
const ShowRentACarObject = { template: '<show-rentacar-object></show-rentacar-object>' }
const ShowCart = { template: '<show-cart></show-cart>' }
const EditProfileData = { template: '<edit-profile></edit-profile>'}
const ShowUsers = { template: '<show-users></show-users>' }

const router = new VueRouter({
	mode: 'hash',
	  routes: [
		{ path: '/', component: HomePage},
		{ path: '/register', name: 'register', component: Register},
		{ path: '/login', component: Login},
		{ path: '/profileOverview', component: ProfileOverview},
		{ path: '/createRentACarObject', component: CreateRentACarObject},
		{ path: '/showRentACarObject/:id', component: ShowRentACarObject },
		{ path: '/showCart', component: ShowCart },
		{ path: '/editProfileData', component: EditProfileData},
		{ path: '/showUsers', component: ShowUsers},
	  ]
});

var app = new Vue({
	router,
	el: '#registrationForm'
});