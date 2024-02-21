Vue.component("show-cart", {
  data: function () {
    return {
      allVehicles: [],
      totalPrice: 0,
      user: null,
    }
  },

  template:
    `
  <div class="containerCreatePorudzbina">
    <h2 class="login-title" style="padding-left: 43rem">Cart</h2>

    <div id="carTableKorpa">

      <table id="vozilaTableKorpa">
        <tr>
          <th colspan="5">Items in cart</th>
        </tr>
        <tr>
          <th>Brand</th>
          <th>Model</th>
          <th>Price</th>
          <th>Image</th>
          <th></th>
        </tr>
        <tr v-for="v in allVehicles">
          <td>{{v.brand}}</td>
          <td>{{v.model}}</td>
          <td>{{v.price}}</td>
          <td><img :src="v.image" alt="Image" id="slika-image-k"></td>
          <td><button class="btnn" v-on:click='removeFromCart(v)' id="innerButtonK"> Remove </button></td>
        </tr>
      </table>

      <div id="sumCena" style="margin-left:42.5rem">
        <label class="labels">Total price: </label>
        <label class="labels">{{ totalPrice }}</label>
        </div>
        <button class="btn" style="margin-left:38rem" id="buttonKorpa" v-on:click='rentClick()'>Rent</button>
    </div>
  </div>

	`,
  mounted() {
    axios.get('rest/users/getByUsername/' + this.getCookie("username"))
      .then(response => {
        this.user = response.data;

        axios.get('rest/carts/getAllForCustomer/' + this.user.id)
          .then(response => {
            this.allVehicles = response.data;
            for (let i = 0; i < this.allVehicles.length; i++) {
              this.totalPrice += this.allVehicles[i].price;
            }
          })
      });



  },

  methods: {
    removeFromCart: function (vehicleForRemove) {
      axios.delete('rest/carts/deleteVehicle/' + vehicleForRemove.id + '/' + this.user.id)
        .then(response => {

          for (let i = 0; i < this.allVehicles.length; i++) {
            if (this.allVehicles[i].id == vehicleForRemove.id) {
              this.allVehicles.splice(i, 1);
              this.totalPrice -= vehicleForRemove.price;
              break;
            }
          }

        });
    },

    rentClick: function () {
      axios.post('rest/orders/makeOrder/' + this.user.id, this.allVehicles)
        .then(response => {
          console.log(response.data);
          this.allVehicles = null;

          axios.put('rest/users/generatePoints/' + this.user.id + '/' + this.totalPrice)
            .then(response => {
              console.log(response.data);
              this.totalPrice = 0;
            })

        })
    },

    getCookie: function(cname) {
			let name = cname + "=";
			let decodedCookie = decodeURIComponent(document.cookie);
			let ca = decodedCookie.split(';');
			for(let i = 0; i <ca.length; i++) {
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

  },

})