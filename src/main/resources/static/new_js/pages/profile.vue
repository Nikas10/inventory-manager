<template>
  <c-default-page>
    <b-container>
      <b-card title="Profile">
        <b-form>
          <b-form-group label="Login:">
            <b-form-input id="login" v-model="form.login" required placeholder="Login"></b-form-input>
          </b-form-group>

          <b-form-group label="Email:">
            <b-form-input id="login" v-model="form.email" required placeholder="Email"></b-form-input>
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
  data: function() {
    return {
      storage: storage,
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
  mounted() {
    // Clone
    if (storage.user != null) {
      this.form = JSON.parse(JSON.stringify(storage.user));
    }
  }
};
</script>
