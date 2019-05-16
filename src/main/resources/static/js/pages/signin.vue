<template>
  <c-default-page :storage="storage">
    <b-container>
      <b-form @submit="onSubmit" class="border border-light p-5">
        <p class="h4 mb-4 text-center">Sign in</p>

        <b-form-group>
          <b-form-input id="login" v-model="form.login" required placeholder="Login"></b-form-input>
        </b-form-group>

        <b-form-group>
          <b-form-input
            id="password"
            v-model="form.password"
            type="password"
            required
            placeholder="Password"
          ></b-form-input>
        </b-form-group>

        <b-button type="submit" block variant="primary">Sign In</b-button>
      </b-form>
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
        login: "",
        password: ""
      }
    };
  },
  methods: {
    onSubmit(evt) {
      const self = this;

      this.$root.auth(this.form.login, this.form.password, function() {
        self.$router.go(-1);
      });

      evt.preventDefault();
    }
  }
};
</script>
