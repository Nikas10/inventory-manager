<template>
  <c-default-page>
    <b-container>
      <b-card title="Profile">
        {{storageUser}}
        {{form}}
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
        </b-form>
      </b-card>
    </b-container>
  </c-default-page>
</template>

<script>
module.exports = {
  components: {
    "c-default-page": httpVueLoader("new_js/components/c-default-page.vue")
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
  methods: {
    loadProfileComponent: function() {
      if (this.storage && this.storage.user) {
        let copyUser = JSON.parse(JSON.stringify(this.storage.user));
        Object.assign(this.form, copyUser);
      } else {
        Object.assign(this.form, {
          email: "",
          login: "",
          password: "",
          firstName: "",
          middleName: "",
          lastName: ""
        });
      }
    }
  },
  mounted() {
    this.loadProfileComponent();
  },
  watch: {
    storage: function() {
      this.loadProfileComponent();
    }
  }
};
</script>
