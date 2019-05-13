<template>
  <c-default-page :storage="storage">
    <b-container>
      <h1>Requisitions</h1>
        <b-button to="/requisitions/new">Create New</b-button>

        <b-form-select v-model="filter.scope" :options="filterOptions"></b-form-select>
      <b-table small :items="requisitions" :fields="fields"></b-table>
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
        name: {
          label: "Name",
          sortable: true
        },
        status: {
          label: "Status",
          sortable: true
        }
      },
      filterOptions: [
        //{value: 'none', text: "All"},
        { value: "all", text: "All" },
        { value: "user", text: "Only mine own" }
      ],
      requisitions: [],
      selectedFilter: "all",
      filter: {
        scope: "none"
      }
    };
  },
  methods: {
    loadPage: function() {
      if (!this.storage.user) {
        // TODO
        return;
      }

      if (this.storage.user.role == "user") {
        this.loadOwnedRequisitions();
      } else {
        this.loadAllRequisitions();
      }
    },
    loadAllRequisitions: function() {
      const self = this;

      this.$server
        .get("/requisitions/")
        .then(function(response) {
          self.requisitions = response.data;
        })
        .catch(function(error) {
          // TODO
        });
    },
    loadOwnedRequisitions: function() {
      const self = this;
      this.$server
        .get("/accounts/" + this.storage.user.login + "/requisitions/")
        .then(function(response) {
          self.requisitions = response.data;
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
    "storage.user": function(a, b) {
      // TODO не очень хорошо получается, что метод дёргается 2 раза, но пока так.
      if (!this.storage.user) {
        return;
      }

      if (["admin", "staff"].includes(this.storage.user.role)) {
        this.filter.scope = "all";
      } else {
        this.filter.scope = "user";
      }
    },
    filter: {
      handler: function(a, b) {
        this.loadPage();
      },
      deep: true
    }
  }
};
</script>
