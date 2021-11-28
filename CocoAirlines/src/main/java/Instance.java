

	enum Instance {
		inst1(6, 18, new int[] { 4, 11 }),

		inst2(7, 25, new int[] { 7 }),

		inst3(7, 25, new int[] { 8 }),

		inst4(10, 60, new int[] { 10, 20 }),

		inst5(11, 75, new int[] { 10, 20, 30, 40, 50 }),

		inst6(12, 100, new int[] { 10, 20, 30, 40, 50, 60, 70, 80, 90 }),

		inst7(13, 200, new int[] { 50, 100, 150 }),

		inst8(14, 125, new int[] { 50, 100 }),;

		final int nb_dividers, capacity;
		final int[] exits;

		Instance(int nbd, int c, int[] e) {
			this.nb_dividers = nbd;
			this.capacity = c;
			this.exits = e;
		}

		int nb_exits() {
			return exits.length;
		}

		int exit(int at) {
			return exits[at];
		}
	}