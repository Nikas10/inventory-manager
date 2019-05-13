<template>
  <c-default-page :storage="storage">
    <b-container>
      <h1>Holder</h1>
      <b-form>
        <b-form-group label="Name:">
          <b-form-input
            id="login"
            :disabled="!changesAllowed"
            v-model="form.name"
            required
            placeholder="Login"
          ></b-form-input>
        </b-form-group>
        <b-form-group label="Description:">
          <b-form-textarea
            id="email"
            :disabled="!changesAllowed"
            v-model="form.description"
            placeholder="Description"
          ></b-form-textarea>
        </b-form-group>
        <b-button v-if="changesAllowed" v-on:click="saveHolder">Save Changes</b-button>
      </b-form>
      <h2>Roles</h2>
      <b-table small :items="roles" :fields="rolesFields">
        <template slot="name" slot-scope="data">
          <b-link :to="'/roles/' + data.item.id">{{data.value}}</b-link>
        </template>
      </b-table>
      <h2>Users associated with holder</h2>
      <b-table small :items="users" :fields="usersFields">
        <template slot="login" slot-scope="data">
          <b-link :to="'/users/' + data.item.login">{{data.value}}</b-link>
        </template>
      </b-table>
      <h2>Items</h2>
      <b-table small :items="items" :fields="itemsFields">
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
      form: {
        name: "",
        description: ""
      },
      usersFields: {
        login: {
          label: "Login",
          sortable: true
        }
      },
      rolesFields: {
        name: {
          label: "Name",
          sortable: true
        }
      },
      itemsFields: {
        inventoryPosition: {
          label: "InventoryPosition",
          sortable: true
        },
        status: {
          label: "Status",
          sortable: true
        },
        amount: {
          label: "Amount",
          sortable: true
        }
      },
      users: [],
      roles: [],
      items: []
    };
  },
  computed: {
    changesAllowed: function() {
      if (!this.storage.user) {
        return false;
      }

      return ["admin", "staff"].includes(this.storage.user.role);
    }
  },
  methods: {
    loadPage: function() {
      this.loadHolder();
      this.loadUsers();
      this.loadRoles();
      this.loadItems();
    },
    loadHolder: function() {
      const self = this;
      const holderId = this.$route.params.id;

      this.$server
        .get("/holders/" + holderId)
        .then(function(response) {
          self.form = { ...self.form, ...response.data };
        })
        .catch(function(error) {
          var response = error.response;
          if (response) {
            if (response.status == 404) {
              alert("TODO Holder not found");
            }
          }
        });
    },
    saveHolder: function() {
      const holderId = this.$route.params.id;

      this.$server
        .patch("/holders/" + holderId, {
          name: this.form.name,
          description: this.form.description
        })
        .then(function(response) {});
    },
    loadUsers: function() {
      const self = this;
      const holderId = this.$route.params.id;

      this.$server
        .get("/holders/" + holderId + "/accounts/")
        .then(function(response) {
          self.users = response.data;
        })
        .catch(function(error) {
          // TODO
        });
    },
    loadRoles: function() {
      const self = this;
      const holderId = this.$route.params.id;

      this.$server
        .get("/holders/" + holderId + "/roles/")
        .then(function(response) {
          self.roles = response.data;
        })
        .catch(function(error) {
          // TODO
        });
    },
    loadItems: function() {
      const self = this;
      const holderId = this.$route.params.id;

      this.$server
        .get("/holders/" + holderId + "/items/")
        .then(function(response) {
          self.items = response.data;
        })
        .catch(function(error) {
          // TODO
        });
    }
  },
  mounted: function() {
    this.loadPage();
  },

  watch: {
    $route: function(to, from) {
      this.loadPage();
    }
  }
};
</script>
