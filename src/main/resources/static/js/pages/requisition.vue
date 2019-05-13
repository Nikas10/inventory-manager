<template>
  <c-default-page :storage="storage">
    <b-container>
      <h1>Requisition</h1>

      <b-form-group label="Description:">
        <b-form-textarea
          id="description"
          disabled
          v-model="form.description"
          required
          placeholder="Description"
        ></b-form-textarea>
      </b-form-group>

      <b-form-group label="Creator:">
        <b-form-input id="creator" disabled v-model="form.login" required placeholder="Creator"></b-form-input>
      </b-form-group>

      <b-form-group label="Assigned To:">
        <b-form-input
          id="assigned"
          disabled
          v-model="form.assigned"
          required
          placeholder="Assigned"
        ></b-form-input>
      </b-form-group>

      <b-form-group label="Status:">
        <b-form-input id="status" disabled v-model="form.status" required placeholder="Status"></b-form-input>
      </b-form-group>

      <b-form-group label="Due Date:">
        <b-form-input id="dueDate" disabled v-model="form.dueDate" type="date" required></b-form-input>
      </b-form-group>

      <b-form-group label="Creation Date:">
        <b-form-input id="creationDate" disabled v-model="form.creationDate" type="date" required></b-form-input>
      </b-form-group>

      <h2>
        Requested Positions
      </h2>

      <b-table small :items="positions" :fields="positionsFields">

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
        description: "",
        login: "",
        status: "new",
        creationDate: this.formatDate(Date.now()),
        dueDate: null,
        assigned: ""
      },
      positionsFields: {
        name: {
          label: "Name",
          sortable: true
        },
        amount: {
          label: "Amount",
          sortable: true
        }
      },
      requisition: {},
      positions: []
    };
  },
  methods: {
    appendZeroes: function(n) {
      if (n <= 9) {
        return "0" + n;
      }
      return n;
    },
    formatDate(string) {
      let date = new Date(string);

      let year = date.getFullYear();
      let month = this.appendZeroes(date.getMonth() + 1)
      let day = this.appendZeroes(date.getDate());

      return year + "-" + month + "-" + day;
    },
    loadPage: function() {
      if (this.$route.params.id != "new") {
        this.loadRequisition();
        this.loadPositions();
      }
    },
    loadRequisition: function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      this.$server
        .get("/requisitions/" + requisitonId)
        .then(function(response) {
          self.form = response.data;
        });
    },
    loadPositions: function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      this.$server
        .get("/requisitions/" + requisitonId + '/positions/')
        .then(function(response) {
          self.positions = response.data;
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
