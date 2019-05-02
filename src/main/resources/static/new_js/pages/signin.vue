<template>
  <c-default-page>
    <b-container>
      <b-card title="Sign In">
        <b-form @submit="onSubmit">
          <b-form-group label="Login:">
            <b-form-input id="login" v-model="form.login" required placeholder="Login"></b-form-input>
          </b-form-group>

          <b-form-group label="Password:">
            <b-form-input
              id="password"
              v-model="form.password"
              type="password"
              required
              placeholder="Password"
            ></b-form-input>
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
    "c-default-page": httpVueLoader("new_js/components/c-default-page.vue")
  },
  data: function() {
    return {
      storage: storage,
      form: {
        login: "",
        password: ""
      }
    };
  },
  methods: {
    onSubmit(evt) {
      this.$root.auth(this.form.login, this.form.password);

      evt.preventDefault();
    }
  }
};
</script>
