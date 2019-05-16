<template>
  <c-default-page :storage="storage">
    <b-container>

      <b-form v-if="changesAllowed">
        <h1>Create new requirement</h1>
        <b-row>
          <b-col>
            <b-form-input
            id="name"
            :disabled="!changesAllowed"
            v-model="form.name"
            required
            placeholder="Inventory position name"
          ></b-form-input>
          </b-col>
          <b-col>
            <b-button variant="primary" block v-on:click="saveRequirement">Create New</b-button>
          </b-col>
        </b-row>
      </b-form>

      <h1>Requirements</h1>
      <b-table small :items="requirements" :fields="fields"></b-table>
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
        name: ""
      },
      fields: {
        name: {
          label: "Name",
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
    loadRequirements: function() {
      const self = this;
      this.$server.get("/requirements/").then(function(response) {
        self.requirements = response.data;
      });
    }
  },
  mounted: function() {
    this.loadRequirements();
  }
};
</script>
