<template>
  <c-default-page :storage="storage">
    <b-container>
      <h1>Users</h1>
      <b-table small :items="users" :fields="fields">
        <template slot="login" slot-scope="data">
          <b-link :to="'/users/' + data.item.login">{{data.value}}</b-link>
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
      fields: {
        login: {
          label: "Login",
          sortable: true
        },
        email: {
          label: "Email",
          sortable: true
        },
        role: {
          label: "Role",
          sortable: true
        }
      },
      users: []
    };
  },
  methods: {
    loadUsers: function() {
      const self = this;
      this.$server.get("/accounts/").then(function(response) {
        self.users = response.data;
      });
    }
  },
  mounted: function() {
    this.loadUsers();
  }
};
</script>
