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

      <b-container v-if="this.$route.params.id !== 'new' ">
        <h2>Requirements</h2>
        <b-table small :items="requirements" :fields="requirementsFields"></b-table>

        <h2>Bundle Parts</h2>
        <b-table small :items="bundleParts" :fields="bundlePartsFields">
          <template slot="name" slot-scope="data">
            <b-link :to="'/positions/' + data.item.stringPositionId">{{data.value}}</b-link>
          </template>
        </b-table>
      </b-container>
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
        description: "",
        bunde: false
      },
      requirementsFields: {
        name: {
          label: "Name",
          sortable: true
        },
        value: {
          label: "Value",
          sortable: true
        }
      },
      bundlePartsFields: {
        positionName: {
          label: "Name",
          sortable: true
        },
        amount: {
          label: "Ammount",
          sortable: true
        }
      },
      requirements: [],
      bundleParts: []
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
      this.loadBundleParts();
    },
    loadInventoryPosition() {
      const self = this;
      const positionId = this.$route.params.id;

      return this.$server
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

      return this.$server
        .patch("/positions/" + positionId, {
          name: this.form.name,
          description: this.form.description
        })
        .then(function(response) {});
    },
    loadRequirements: function() {
      const self = this;
      const positionId = this.$route.params.id;

      return this.$server
        .get("/positions/" + positionId + "/requirements/")
        .then(function(response) {
          self.requirements = response.data;
        })
        .catch(function(error) {
          // TODO
        });
    },
    loadBundleParts: function() {
      const self = this;
      const positionId = this.$route.params.id;

      return this.$server
        .get("/positions/" + positionId + "/bundleParts/")
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
  }
};
</script>
