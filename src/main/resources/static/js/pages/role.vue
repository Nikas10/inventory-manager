<template>
  <c-default-page :storage="storage">
    <b-container>
      <h1>Role</h1>
      <b-form>
        <b-form-group label="Name:">
          <b-form-input
            id="name"
            :disabled="!changesAllowed"
            v-model="form.name"
            required
            placeholder="Name"
          ></b-form-input>
        </b-form-group>
        <b-form-group label="Description:">
          <b-form-textarea
            id="description"
            :disabled="!changesAllowed"
            v-model="form.description"
            placeholder="Description"
          ></b-form-textarea>
        </b-form-group>
        <b-button v-if="changesAllowed" v-on:click="saveRole">Save Changes</b-button>
      </b-form>

      <h2>Inventory Positions</h2>
      <b-table small :items="inventoryPositions" :fields="inventoryPositionFields">
        <template slot="name" slot-scope="data">
          <b-link :to="'/positions/' + data.item.id">{{data.value}}</b-link>
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
      inventoryPositionFields: {
         name: {
          label: "Name",
          sortable: true
        },
        bundle: {
          label: "Bundle",
          sortable: true
        }
      },
      inventoryPositions: []
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
      this.loadRole();
      this.loadInventoryPositions();
    },
    loadRole() {
      const self = this;
      const roleId = this.$route.params.id;

      this.$server
        .get("/roles/" + roleId)
        .then(function(response) {
          self.form = { ...self.form, ...response.data };
        })
        .catch(function(error) {
          var response = error.response;
          if (response) {
            if (response.status == 404) {
              alert("TODO Position not found");
            }
          }
        });
    },
    saveRole() {
      const roleId = this.$route.params.id;

      this.$server
        .patch("/roles/" + roleId, {
          name: this.form.name,
          description: this.form.description
        })
        .then(function(response) {});
    },
    loadInventoryPositions() {
      const self = this;
      const roleId = this.$route.params.id;

      this.$server
        .get("/roles/" + roleId + "/positions/")
        .then(function(response) {
          self.inventoryPositions = response.data;
        })
        .catch(function(error) {
          // TODO
        });
    }
  },
  mounted: function() {
    this.loadPage();
  },
};
</script>
