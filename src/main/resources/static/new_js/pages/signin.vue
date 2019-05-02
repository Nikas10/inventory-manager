<template>
  <c-default-page>
    <b-container>
      <b-card title="Sign In">
        <b-form @submit="onSubmit">
          <b-form-group label="Login:">
            <b-form-input id="login" v-model="form.login" required placeholder="Login"></b-form-input>
          </b-form-group>

          <b-form-group label="Password:">
            <b-form-input id="password" v-model="form.password" required placeholder="Password"></b-form-input>
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
      // TODO Добавить обработку ошибок

      var self = this;

      this.$root.unsetToken();
      this.storage.user = null;

      this.$root
        .auth(this.form.login, this.form.password)
        .then(function(response) {
          var token = response.data;

          setCookie("auth", token.access_token, token.expires_in);

          self.$root.setToken(token.access_token);

          self.$server.get("/account").then(function(response) {
            self.storage.user = {
              name: response.data.login
            };
            // TODO Добавить перенаправление на другую страницу
          });
        });

      evt.preventDefault();
    }
  }
};
</script>
