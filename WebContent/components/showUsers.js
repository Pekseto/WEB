Vue.component("show-users", {
  data: function () {
    return {
      searchParams: {
        firstName: "",
        lastName: "",
        username: "",
      },
      filterParams: {
        role: "",
        type: "",
      },
      sortParams: {
        sortBy: "",
        sortType: "",
      },
      users: null,
      suspiciousCustomers: null,
    }
  },

  template:
    `
    <div class="main-container">
      <div class="top-container">
        <div class="search-wrapper" style="margin-top:3rem;">
          <input class="search_input" type="text" placeholder="First name" v-model="searchParams.firstName">
          <input class="search_input" type="text" placeholder="Last name" v-model="searchParams.lastName">
          <input class="search_input" type="text" placeholder="Username" v-model="searchParams.username">
          <button class="btnn btn-search" v-on:click="search()">Search</button>
          <button class="btnn btn-search" type="button" v-on:click="refreshList()">Refresh</button>
        </div>

        <div class="filters-wrapper">
          <select class="search_input" id="roleFilter" v-model="filterParams.role">
            <option value="Customer">Customer</option>
            <option value="Manager">Manager</option>
            <option value="Administrator">Administrator</option>
          </select>
          <button class="btnn btn-search" type="button" v-on:click="filterByRole()">Filter</button>
          <select class="search_input" id="customerTypeFilter" v-model="filterParams.type">
            <option value="Bronze">Bronze</option>      
            <option value="Silver">Silver</option>
            <option value="Gold">Gold</option>
          </select>
          <button class="btnn btn-search" type="button" v-on:click="filterByType()">Filter</button>
        </div>
      </div>
      <div class="sort-container">
        <select class="search_input" id="sortColumn" v-model="sortParams.sortBy">
          <option value="firstName">First name</option>
          <option value="lastName">Last name</option>
          <option value="username">Username</option>
          <option value="points">Points</option>
        </select>
        <select class="search_input" id="sortFilter" v-model="sortParams.sortType">
          <option value="Asc">Asc</option>
          <option value="Desc">Desc</option>
        </select>
        <button class="btnn btn-search btn-sort" type="button" v-on:click="sort()">Sort</button>
      </div>

        <h3>All users</h3>
        <table>
          <tr>
            <th>Username</th>
            <th>First name</th>
            <th>Last name</th>
            <th>Gender</th>
            <th>Role</th>
            <th>Date of birth</th>
            <th>Block/Unblock</th>
          </tr>
          <tr v-for="user in users">
            <td>{{user.username}}</td>
            <td>{{user.firstName}}</td>
            <td>{{user.lastName}}</td>
            <td>{{user.gender}}</td>
            <td>{{user.role}}</td>
            <td>{{user.dateOfBirth}}</td>
            <td v-if="user.role != 'Administrator'">
              <button class="btnn blocked" v-if="!user.blocked" v-on:click="blockUser(user)">Block</button>
              <button class="btnn" v-if="user.blocked" v-on:click="blockUser(user)">Unblock</button>
            </td>
          </tr>
        </table><br>

        <h3>Suspicious customers</h3>
        <table>
          <tr>
            <th>Username</th>
            <th>First name</th>
            <th>Last name</th>
            <th>Gender</th>
            <th>Role</th>
            <th>Date of birth</th>
            <th>Block/Unblock</th>
          </tr>
          <tr v-for="user in suspiciousCustomers">
            <td>{{user.username}}</td>
            <td>{{user.firstName}}</td>
            <td>{{user.lastName}}</td>
            <td>{{user.gender}}</td>
            <td>{{user.role}}</td>
            <td>{{user.dateOfBirth}}</td>
            <td v-if="user.role != 'Administrator'">
              <button class="btnn blocked" v-if="!user.blocked" v-on:click="blockUser(user)">Block</button>
              <button class="btnn" v-if="user.blocked" v-on:click="blockUser(user)">Unblock</button>
            </td>
          </tr>
        </table>
  </div>
    `,

  mounted() {
    axios.get('rest/users/getAll')
      .then(response => {
        this.users = response.data;
      })
    axios.get('rest/users/suspiciousCustomers')
      .then(response => {
        this.suspiciousCustomers = response.data;
      })
  },

  methods: {

    search: function () {
      axios.post('rest/users/getBySearch/', this.searchParams)
        .then(response => {
          this.users = response.data;
        })
    },

    refreshList: function () {
      axios.get('rest/users/getAll')
        .then(response => {
          this.users = response.data;
        })
    },

    filterByRole: function () {
      axios.get('rest/users/filterByRole/' + this.filterParams.role)
        .then(response => {
          this.users = response.data;
        })
    },

    filterByType: function () {
      axios.get('rest/users/filterByType/' + this.filterParams.type)
        .then(response => {
          this.users = response.data;
        })
    },

    sort: function () {
      axios.get('rest/users/sort/' + this.sortParams.sortBy + '/' + this.sortParams.sortType)
        .then(response => {
          this.users = response.data;
        })
    },

    blockUser: function (userForBlock) {
      axios.put('rest/users/block/', userForBlock)
        .then(response => {
          axios.get('rest/users/getAll')
            .then(response => {
              this.users = response.data;
            })
          axios.get('rest/users/suspiciousCustomers')
            .then(response => {
              this.suspiciousCustomers = response.data;
            })
        })
    },

  }
})

var link = document.createElement("link");
link.rel = "stylesheet";
link.type = "text/css";
link.href = "css/showUsers.css";
var firstLink = document.getElementsByTagName("link")[0];
document.head.insertBefore(link, firstLink);