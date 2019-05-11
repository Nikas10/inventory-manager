<template>
  <c-default-page :storage="storage">
    <b-container>
      <b-card title="Sign Up">
        <b-form @submit="onSubmit">
          <b-form-group label="Email address:">
            <b-form-input
              id="email"
              v-model="form.email"
              type="email"
              required
              placeholder="email@example.com"
            ></b-form-input>
          </b-form-group>

          <b-form-group label="Login:">
            <b-form-input id="login" v-model="form.login" required placeholder="Login"></b-form-input>
          </b-form-group>

          <b-form-group label="Password:">
            <b-form-input id="password" v-model="form.password" type="password" required placeholder="Password"></b-form-input>
          </b-form-group>

          <b-form-group label="First Name:">
            <b-form-input id="firstName" v-model="form.firstName" required placeholder="First Name"></b-form-input>
          </b-form-group>

          <b-form-group label="Middle Name:">
            <b-form-input
              id="middleName"
              v-model="form.middleName"
              required
              placeholder="Middle Name"
            ></b-form-input>
          </b-form-group>

          <b-form-group label="Last Name:">
            <b-form-input id="lastName" v-model="form.lastName" required placeholder="Last Name"></b-form-input>
          </b-form-group>

          <b-button type="submit" variant="primary">Submit</b-button>
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
  methods: {
    onSubmit(evt) {
      this.$server.post("/accounts", this.form).then(function() {
        // TODO Добавить обработку ошибок.
        // TODO Добавить сообщение об успешной регистрации.
      });

      evt.preventDefault();
    }
  }
};
</script>
