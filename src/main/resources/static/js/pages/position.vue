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
        <b-form-group label="Is bundle?">
          <b-form-checkbox id="bundle" v-model="form.bundle"></b-form-checkbox>
        </b-form-group>

        <b-button
          v-if="changesAllowed && this.$route.params.id !== 'new' "
          v-on:click="saveInventoryPosition"
        >Save Changes</b-button>
        <b-button
          v-if="changesAllowed && this.$route.params.id === 'new' "
          v-on:click="createInventoryPosition"
        >Save Changes</b-button>
      </b-form>

      <b-container v-if="this.$route.params.id !== 'new' ">
        <h2>Requirements</h2>
        <b-table small :items="requirements" :fields="requirementsFields"></b-table>

        <b-container v-if="orig.bundle">
          <h2>Bundle Parts</h2>
          <b-table small :items="newBundleParts" :fields="bundlePartsFields">
            <template slot="positionName" slot-scope="data">
              <b-link :to="'/positions/' + data.item.stringPositionId">{{data.value}}</b-link>
            </template>
          </b-table>
        </b-container>
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
      orig: {
        name: "",
        description: "",
        bundle: false
      },
      form: {
        name: "",
        description: "",
        bundle: false
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
      newRequirements: [],
      bundleParts: [],
      newBundleParts: []
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
    loadPage: async function() {
      if (this.$route.params.id !== "new") {
        await this.loadInventoryPosition();
        await this.loadRequirements();
        await this.loadBundleParts();
      }
    },
    loadInventoryPosition: async function() {
      const self = this;
      const positionId = this.$route.params.id;

      return this.$server
        .get("/positions/" + positionId)
        .then(function(response) {
          self.orig = { ...self.orig, ...response.data };
          self.form = deepClone(self.orig);
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
    saveInventoryPosition: async function() {
      const self = this;
      const positionId = this.$route.params.id;
      let body = objDiff(this.orig, this.form);

      return this.$server
        .patch("/positions/" + positionId, body)
        .then(function(response) {
          self.loadInventoryPosition();
        });
    },
    createInventoryPosition: async function() {
      const self = this;
      const positionId = this.$route.params.id;

      return this.$server
        .post("/positions/", {
          name: self.form.name,
          description: self.form.description,
          bundle: self.form.bundle
        })
        .then(function(response) {
          //TODO redirect
        });
    },
    loadRequirements: async function() {
      const self = this;
      const positionId = this.$route.params.id;

      return this.$server
        .get("/positions/" + positionId + "/requirements/")
        .then(function(response) {
          self.requirements = response.data;
          self.newRequirements = deepClone(self.requirements);
        })
        .catch(function(error) {
          // TODO
        });
    },
    loadBundleParts: async function() {
      const self = this;
      const positionId = this.$route.params.id;

      return this.$server
        .get("/positions/" + positionId + "/bundleParts/")
        .then(function(response) {
          self.bundleParts = response.data;
          self.newBundleParts = deepClone(self.bundleParts);
        })
        .catch(function(error) {
          // TODO
        });
    }
  },
  mounted: function() {
    this.loadPage();
  }
};
</script>
