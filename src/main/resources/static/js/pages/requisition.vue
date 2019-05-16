<template>
  <c-default-page :storage="storage">
    <b-container>
      <h1>Requisition</h1>

      <b-form>
        <b-form-group label="Description:">
          <b-form-textarea
            id="description"
            :disabled="!changesAllowed"
            v-model="forms.requisition.description"
            required
            placeholder="Description"
          ></b-form-textarea>
        </b-form-group>

        <b-form-group label="Creator:">
          <b-form-input
            id="creator"
            disabled
            v-model="forms.requisition.login"
            required
            placeholder="Creator"
          ></b-form-input>
        </b-form-group>

        <b-form-group label="Holder:">
          <b-form-select
            :disabled="forms.requisition.status != 'NEW'"
            v-model="forms.requisition.holderUUID"
            :options="holdersOptions"
          ></b-form-select>
        </b-form-group>

        <b-form-group label="Assigned To:">
          <b-form-input
            id="assigned"
            disabled
            v-model="forms.requisition.assigned"
            required
            placeholder="Assigned"
          ></b-form-input>
        </b-form-group>

        <b-form-group label="Status:">
          <b-form-input
            id="status"
            disabled
            v-model="forms.requisition.status"
            required
            placeholder="Status"
          ></b-form-input>
        </b-form-group>
        <b-form-group label="Due Date:">
          <b-form-input
            id="dueDate"
            :disabled="!changesAllowed"
            v-model="forms.requisition.dueDate"
            type="date"
            required
          ></b-form-input>
        </b-form-group>

        <b-form-group label="Creation Date:">
          <b-form-input
            id="creationDate"
            :disabled="!changesAllowed"
            v-model="forms.requisition.creationDate"
            type="date"
            required
          ></b-form-input>
        </b-form-group>
      </b-form>

      <b-form-group>
        <b-button v-if="approveAllowed" @click="setStatusApproved">Approve</b-button>

        <b-button
          v-if="clarificationAllowed"
          @click="setStatusRequiredClarification"
        >Require Clarification</b-button>

        <b-button v-if="rejectAllowed" @click="setStatusRejected">Reject</b-button>

        <b-button v-if="completeAllowed" @click="setStatusCompleted">Complete</b-button>

        <b-button v-if="completeChangeAllowed" @click="setStatusCompletedChanges">Complete Changes</b-button>

        <b-button v-if="createAllowed" @click="createRequisition">Create</b-button>

        <b-button v-if="updateAllowed" @click="updateRequisition">Update</b-button>
      </b-form-group>
      <h2>Requested Positions</h2>

      <b-form inline>
        <b-form-input v-model="forms.position.amount" type="number" min="1"></b-form-input>
        <b-col>
          <b-form-select v-model="forms.position.id" :options="positionOptions"></b-form-select>
        </b-col>
        <b-button variant="primary" @click="addNewPosition">Add position</b-button>
      </b-form>

      <br>

      <b-table small :items="newPositions" :fields="positionsFields"></b-table>
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
      forms: {
        requisition: {
          description: "",
          login: "",
          status: "NEW",
          creationDate: this.formatDate(Date.now()),
          dueDate: this.formatDate(
            new Date().setDate(new Date().getDate() + 14)
          ),
          assigned: "",
          holderName: "",
          holderUUID: ""
        },
        position: {
          amount: 1,
          id: "0000-0001"
        }
      },
      positionsFields: {
        amount: {
          label: "Amount",
          sortable: true
        },
        name: {
          label: "Name",
          sortable: true
        },
        actions: {
          label: "Actions",
          sortable: true
        }
      },
      requisition: {},
      positions: [],
      newPositions: [],
      availablePositions: [
        {
          id: "0000-0001",
          name: "Pos 1"
        },
        {
          id: "0000-0002",
          name: "Pos 2"
        },
        {
          id: "0000-0003",
          name: "Pos 3"
        }
      ],
      availableHolders: []
    };
  },
  computed: {
    isStaff: function() {
      if (!this.storage.user) {
        return false;
      }

      return ["staff", "admin"].includes(this.storage.user.role);
    },
    changesAllowed: function() {
      if (!this.storage.user) {
        return false;
      }

      const isNew = this.forms.requisition.status == "NEW";
      const needsClarification =
        this.forms.requisition.status == "REQUIRED_CLARIFICATION";

      return isNew || needsClarification || this.isStaff;
    },
    updateAllowed: function() {
      const isNew = this.forms.requisition.status == "NEW";
      const isCompleted = this.forms.requisition.status == "COMPLETED";
      const isApproved = this.forms.requisition.status == "APPROVED";

      return (!isNew || !isCompleted || !isApproved) && this.isStaff;
    },
    approveAllowed: function() {
      const reviewNeeded = this.forms.requisition.status == "REVIEW_NEEDED";

      return reviewNeeded && this.isStaff;
    },
    rejectAllowed: function() {
      const reviewNeeded = this.forms.requisition.status == "REVIEW_NEEDED";

      return reviewNeeded && this.isStaff;
    },
    clarificationAllowed: function() {
      const reviewNeeded = this.forms.requisition.status == "REVIEW_NEEDED";

      return reviewNeeded && this.isStaff;
    },
    completeAllowed: function() {},
    completeChangeAllowed: function() {},
    createAllowed: function() {
      const isNew = this.forms.requisition.status == "NEW";

      return isNew;
    },
    positionOptions: function() {
      return this.availablePositions.map(function(pos) {
        return { value: pos, text: pos.name };
      });
    },
    holdersOptions: function() {
      return this.availableHolders.map(function(holder) {
        return { value: holder.id, text: holder.name };
      });
    }
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
      let month = this.appendZeroes(date.getMonth() + 1);
      let day = this.appendZeroes(date.getDate());

      return year + "-" + month + "-" + day;
    },
    loadPage: async function() {
      if (this.$route.params.id != "new") {
        await this.loadRequisition();
        this.loadPositions();
      }

      await this.loadHolders();
      //await this.loadAvailablePositions();
    },
    loadRequisition: async function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      return this.$server
        .get("/requisitions/" + requisitonId)
        .then(function(response) {
          self.forms.requisition = response.data;
          self.forms.requisition.dueDate = self.formatDate(
            response.data.dueDate
          );
          self.forms.requisition.creationDate = self.formatDate(
            response.data.creationDate
          );
        });
    },
    loadHolders: async function() {
      const self = this;
      const username = this.storage.user.login;

      if (this.$route.params.id == "new") {
        return this.$server
          .get("/accounts/" + username + "/holders/")
          .then(function(response) {
            self.availableHolders = response.data;
            self.forms.requisition.holderUUID = self.availableHolders[0].id;
          });
      } else {
        this.availableHolders = [
          {
            id: this.forms.requisition.holderUUID,
            name: this.forms.requisition.holderName
          }
        ];
        return Promise.resolve();
      }
    },
    loadPositions: async function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      await this.$server
        .get("/requisitions/" + requisitonId + "/positions/")
        .then(function(response) {
          self.positions = response.data;
          self.newPositions = deepClone(self.positions);
        });
    },
    loadAvailablePositions: async function() {
      const self = this;
      const holderId = this.forms.requisition.holderUUID;

      return this.$server
        .get("/holders/" + holderId + "/availablePositions/")
        .then(function(response) {
          self.availablePositions = response.data;
          self.forms.position.id = self.availablePositions[0];
        });
    },
    setStatusApproved: function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      this.$server
        .patch("/requisitions/" + requisitonId, { status: "APPROVED" })
        .then(function(response) {});
    },
    setStatusRequiredClarification: function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      this.$server
        .patch("/requisitions/" + requisitonId, {
          status: "REQUIRED_CLARIFICATION"
        })
        .then(function(response) {});
    },
    setStatusRejected: function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      this.$server
        .patch("/requisitions/" + requisitonId, { status: "REJECTED" })
        .then(function(response) {});
    },
    setStatusCompleted: function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      this.$server
        .patch("/requisitions/" + requisitonId, { status: "COMPLETED" })
        .then(function(response) {});
    },
    setStatusCompletedChanges: function() {
      const self = this;
      const requisitonId = this.$route.params.id;

      this.$server
        .patch("/requisitions/" + requisitonId, { status: "REVIEW_NEEDED" })
        .then(function(response) {});
    },
    createRequisition: function() {
      const self = this;

      this.$server
        .post("/requisitions/", this.forms.requisition)
        .then(function(response) {});
    },
    addNewPosition: function() {
      // TODO сделать копию positions, чтобы потом по разнице определять какие запросы нужно отправить
      const position = {
        amount: this.forms.position.amount,
        name: this.forms.position.id.name,
        id: this.forms.position.id.id
      };

      this.newPositions.push(position);
    },
    updateRequisition: function() {
      // TODO
    }
  },
  mounted: function() {
    this.loadPage();
  },
  watch: {
    $route: function(to, from) {
      this.loadPage();
    },
    "forms.requisition.holderUUID": function(to, from) {
      //this.loadHolders();
      this.loadAvailablePositions();

      this.newPositions = [];
    }
  }
};
</script>
