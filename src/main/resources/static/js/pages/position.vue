<template>
  <c-default-page :storage="storage">
    <b-container>
      <h1>Inventory position</h1>
      <b-form>
        <b-form-group label="Name:">
          <b-form-input
            id="name"
            :disabled="!changesAllowed"
            v-model="form.name"
            required
            placeholder="Inventory position name"
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
        <b-button v-if="changesAllowed" v-on:click="saveInventoryPosition">Save Changes</b-button>
      </b-form>

      <h2>Requirements</h2>
      <b-table small :items="requirements" :fields="requirementsFields"></b-table>

      <!-- <h1>Bundle Parts</h1>
      <b-table small :items="positions" :fields="fields">
        <template slot="name" slot-scope="data">
          <b-link :to="'/positions/' + data.item.id">{{data.value}}</b-link>
        </template>
      </b-table> -->
    
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
      requirementsFields: {
        requirement: {
          label: "Name",
          sortable: true
        },
         value: {
          label: "Value",
          sortable: true
        }
      },
      requirements: []
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
      this.loadInventoryPosition();
      this.loadRequirements();
    },
    loadInventoryPosition() {
      const self = this;
      const positionId = this.$route.params.id;

      this.$server
        .get("/positions/" + positionId)
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
    saveInventoryPosition: function() {
      const positionId = this.$route.params.id;

      this.$server
        .patch("/positions/" + positionId, {
          name: this.form.name,
          description: this.form.description
        })
        .then(function(response) {});
    },
    loadRequirements: function() {
      const self = this;
      const positionId = this.$route.params.id;

      this.$server
        .get("/positions/" + positionId + "/requirements/")
        .then(function(response) {
          self.requirements = response.data;
        })
        .catch(function(error) {
          // TODO
        });
    },
  },
  mounted: function() {
    this.loadPage();
  },
};
</script>
