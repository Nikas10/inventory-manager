<template>
  <c-default-page :storage="storage">
    <b-container>
      <b-card :title="'User ' + form.login">
        <b-form>
          <b-form-group label="Login:">
            <b-form-input id="login" v-model="form.login" required placeholder="Login"></b-form-input>
          </b-form-group>
          <b-form-group label="Email:">
            <b-form-input id="email" v-model="form.email" required placeholder="Email"></b-form-input>
          </b-form-group>
          <b-form-group label="First Name:">
            <b-form-input id="firstName" v-model="form.firstName" required placeholder="First name"></b-form-input>
          </b-form-group>
          <b-form-group label="Middle Name:">
            <b-form-input
              id="middleName"
              v-model="form.middleName"
              required
              placeholder="Middle name"
            ></b-form-input>
          </b-form-group>
          <b-form-group label="Last Name:">
            <b-form-input id="lastName" v-model="form.lastName" required placeholder="Last name"></b-form-input>
          </b-form-group>
          <b-button v-if="changesAllowed" v-on:click="saveUser">Save Changes</b-button>
        </b-form>
      </b-card>
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
      }
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
      var self = this;
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
