<template>
  <c-default-page :storage="storage">
    <b-container>
      <b-card :title="'User ' + form.login">
        <b-form>
          <b-form-group label="Login:">
            <b-form-input id="login" disabled v-model="form.login" required placeholder="Login"></b-form-input>
          </b-form-group>
          <b-form-group label="Email:">
            <b-form-input
              id="email"
              :disabled="!changesAllowed"
              v-model="form.email"
              required
              placeholder="Email"
            ></b-form-input>
          </b-form-group>
          <b-form-group label="First Name:">
            <b-form-input
              id="firstName"
              :disabled="!changesAllowed"
              v-model="form.firstName"
              required
              placeholder="First name"
            ></b-form-input>
          </b-form-group>
          <b-form-group label="Middle Name:">
            <b-form-input
              id="middleName"
              :disabled="!changesAllowed"
              v-model="form.middleName"
              required
              placeholder="Middle name"
            ></b-form-input>
          </b-form-group>
          <b-form-group label="Last Name:">
            <b-form-input
              id="lastName"
              :disabled="!changesAllowed"
              v-model="form.lastName"
              required
              placeholder="Last name"
            ></b-form-input>
          </b-form-group>
          <b-button v-if="changesAllowed" v-on:click="saveUser">Save Changes</b-button>
        </b-form>
      </b-card>
      <h2>Holders</h2>
      <b-table small :items="holders" :fields="fields">
        <template slot="name" slot-scope="data">
          <b-link :to="'/holders/' + data.item.id">{{data.value}}</b-link>
        </template>
      </b-table>
    </b-container>
  </c-default-page>
</template>

<script>
module.exports = {
  components: {
    "c-default-page": httpVueLoader("js/components/c-default-page.vue")
  },
  props: ["storage"],
  data: function() {
    return {
      form: {
        email: "",
        login: "",
        password: "",
        firstName: "",
        middleName: "",
        lastName: ""
      },
      fields: {
        name: {
          label: "Name",
          sortable: true
        },
        description: {
          label: "Description",
          sortable: true
        }
      },
      holders: []
    };
  },
  computed: {
    changesAllowed: function() {
      if (!this.storage.user) {
        return false;
      }
      var ownProfile =
        this.storage.user && this.storage.user.login == this.form.login;
      var isAdmin = this.storage.user.role == "admin";
      return ownProfile || isAdmin;
    }
  },
  methods: {
    loadUser: function(username) {
      const self = this;

      this.$server
        .get("/account/" + this.$route.params.id)
        .then(function(response) {
          self.form = { ...self.form, ...response.data };
        })
        .catch(function(error) {
          var response = error.response;
          if (response) {
            if (response.status == 404) {
              alert("TODO User not found");
            }
          }
        });

      this.$server
        .get("/account/" + this.$route.params.id + "/holder/")
        .then(function(response) {
          self.holders = response.data;
        });
    },
    saveUser: function() {
      alert("TODO Пока нет обработчика на сервере");
    }
  },
  mounted: function() {
    this.loadUser();
  },
  watch: {
    $route: function(to, from) {
      this.loadUser();
    }
  }
};
</script>
