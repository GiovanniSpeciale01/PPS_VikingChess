teamPPS(["John Mary Special", "Easter Lio", "Mark Cells", "Luke Hnnys"]).
hnefataflAppOfferedBy(P) :- teamPPS(Team), member(P, Team).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%  							   Variants			               			  %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%% Returns the board size of the specified game variant.
% [boardSize(+Variant, -BoardSize)]
boardSize(hnefatafl, 11).

%%% Defines the initial state of the board of the specified game variant
%%% (pieces = e: empty, bp: black pawn, wp: white pawn, wk: white king).
% [initPieces(+Variant, -Board)]
initBoard(
	hnefatafl,
    [
        [ cell(coord(1,1),e),   cell(coord(1,2),e),   cell(coord(1,3),e),  cell(coord(1,4),bp),  cell(coord(1,5),bp),  cell(coord(1,6),bp),  cell(coord(1,7),bp),  cell(coord(1,8),bp),  cell(coord(1,9),e),  cell(coord(1,10),e),   cell(coord(1,11),e)  ],
		[ cell(coord(2,1),e),   cell(coord(2,2),e),   cell(coord(2,3),e),  cell(coord(2,4),e),   cell(coord(2,5),e),   cell(coord(2,6),bp),  cell(coord(2,7),e),   cell(coord(2,8),e),   cell(coord(2,9),e),  cell(coord(2,10),e),   cell(coord(2,11),e)  ],
		[ cell(coord(3,1),e),   cell(coord(3,2),e),   cell(coord(3,3),e),  cell(coord(3,4),e),   cell(coord(3,5),e),   cell(coord(3,6),e),   cell(coord(3,7),e),   cell(coord(3,8),e),   cell(coord(3,9),e),  cell(coord(3,10),e),   cell(coord(3,11),e)  ],
		[ cell(coord(4,1),bp),  cell(coord(4,2),e),   cell(coord(4,3),e),  cell(coord(4,4),e),   cell(coord(4,5),e),   cell(coord(4,6),wp),  cell(coord(4,7),e),   cell(coord(4,8),e),   cell(coord(4,9),e),  cell(coord(4,10),e),   cell(coord(4,11),bp) ],
		[ cell(coord(5,1),bp),  cell(coord(5,2),e),   cell(coord(5,3),e),  cell(coord(5,4),e),   cell(coord(5,5),wp),  cell(coord(5,6),wp),  cell(coord(5,7),wp),  cell(coord(5,8),e),   cell(coord(5,9),e),  cell(coord(5,10),e),   cell(coord(5,11),bp) ],
        [ cell(coord(6,1),bp),  cell(coord(6,2),bp),  cell(coord(6,3),e),  cell(coord(6,4),wp),  cell(coord(6,5),wp),  cell(coord(6,6),wk),  cell(coord(6,7),wp),  cell(coord(6,8),wp),  cell(coord(6,9),e),  cell(coord(6,10),bp),  cell(coord(6,11),bp) ],
        [ cell(coord(7,1),bp),  cell(coord(7,2),e),   cell(coord(7,3),e),  cell(coord(7,4),e),   cell(coord(7,5),wp),  cell(coord(7,6),wp),  cell(coord(7,7),wp),  cell(coord(7,8),e),   cell(coord(7,9),e),  cell(coord(7,10),e),   cell(coord(7,11),bp) ],
        [ cell(coord(8,1),bp),  cell(coord(8,2),e),   cell(coord(8,3),e),  cell(coord(8,4),e),   cell(coord(8,5),e),   cell(coord(8,6),wp),  cell(coord(8,7),e),   cell(coord(8,8),e),   cell(coord(8,9),e),  cell(coord(8,10),e),   cell(coord(8,11),bp) ],
        [ cell(coord(9,1),e),   cell(coord(9,2),e),   cell(coord(9,3),e),  cell(coord(9,4),e),   cell(coord(9,5),e),   cell(coord(9,6),e),   cell(coord(9,7),e),   cell(coord(9,8),e),   cell(coord(9,9),e),  cell(coord(9,10),e),   cell(coord(9,11),e)  ],
        [ cell(coord(10,1),e),  cell(coord(10,2),e),  cell(coord(10,3),e), cell(coord(10,4),e),  cell(coord(10,5),e),  cell(coord(10,6),bp), cell(coord(10,7),e),  cell(coord(10,8),e),  cell(coord(10,9),e), cell(coord(10,10),e),  cell(coord(10,11),e) ],
        [ cell(coord(11,1),e),  cell(coord(11,2),e),  cell(coord(11,3),e), cell(coord(11,4),bp), cell(coord(11,5),bp), cell(coord(11,6),bp), cell(coord(11,7),bp), cell(coord(11,8),bp), cell(coord(11,9),e), cell(coord(11,10),e),  cell(coord(11,11),e) ]
    ]
).

boardSize(tawlbwrdd, 11).
initBoard(
	tawlbwrdd,
    [
        [ cell(coord(1,1),e),   cell(coord(1,2),e),   cell(coord(1,3),e),  cell(coord(1,4),e),   cell(coord(1,5),bp),  cell(coord(1,6),bp),  cell(coord(1,7),bp),  cell(coord(1,8),e),   cell(coord(1,9),e),  cell(coord(1,10),e),   cell(coord(1,11),e)  ],
		[ cell(coord(2,1),e),   cell(coord(2,2),e),   cell(coord(2,3),e),  cell(coord(2,4),e),   cell(coord(2,5),bp),  cell(coord(2,6),e),   cell(coord(2,7),bp),  cell(coord(2,8),e),   cell(coord(2,9),e),  cell(coord(2,10),e),   cell(coord(2,11),e)  ],
		[ cell(coord(3,1),e),   cell(coord(3,2),e),   cell(coord(3,3),e),  cell(coord(3,4),e),   cell(coord(3,5),e),   cell(coord(3,6),bp),  cell(coord(3,7),e),   cell(coord(3,8),e),   cell(coord(3,9),e),  cell(coord(3,10),e),   cell(coord(3,11),e)  ],
        [ cell(coord(4,1),e),   cell(coord(4,2),e),   cell(coord(4,3),e),  cell(coord(4,4),e),   cell(coord(4,5),e),   cell(coord(4,6),wp),  cell(coord(4,7),e),   cell(coord(4,8),e),   cell(coord(4,9),e),  cell(coord(4,10),e),   cell(coord(4,11),e)  ],
        [ cell(coord(5,1),bp),  cell(coord(5,2),bp),  cell(coord(5,3),e),  cell(coord(5,4),e),   cell(coord(5,5),wp),  cell(coord(5,6),wp),  cell(coord(5,7),wp),  cell(coord(5,8),e),   cell(coord(5,9),e),  cell(coord(5,10),bp),  cell(coord(5,11),bp) ],
        [ cell(coord(6,1),bp),  cell(coord(6,2),e),   cell(coord(6,3),bp), cell(coord(6,4),wp),  cell(coord(6,5),wp),  cell(coord(6,6),wk),  cell(coord(6,7),wp),  cell(coord(6,8),wp),  cell(coord(6,9),bp), cell(coord(6,10),e),   cell(coord(6,11),bp) ],
        [ cell(coord(7,1),bp),  cell(coord(7,2),bp),  cell(coord(7,3),e),  cell(coord(7,4),e),   cell(coord(7,5),wp),  cell(coord(7,6),wp),  cell(coord(7,7),wp),  cell(coord(7,8),e),   cell(coord(7,9),e),  cell(coord(7,10),bp),  cell(coord(7,11),bp) ],
        [ cell(coord(8,1),e),   cell(coord(8,2),e),   cell(coord(8,3),e),  cell(coord(8,4),e),   cell(coord(8,5),e),   cell(coord(8,6),wp),  cell(coord(8,7),e),   cell(coord(8,8),e),   cell(coord(8,9),e),  cell(coord(8,10),e),   cell(coord(8,11),e)  ],
        [ cell(coord(9,1),e),   cell(coord(9,2),e),   cell(coord(9,3),e),  cell(coord(9,4),e),   cell(coord(9,5),e),   cell(coord(9,6),bp),  cell(coord(9,7),e),   cell(coord(9,8),e),   cell(coord(9,9),e),  cell(coord(9,10),e),   cell(coord(9,11),e)  ],
        [ cell(coord(10,1),e),  cell(coord(10,2),e),  cell(coord(10,3),e), cell(coord(10,4),e),  cell(coord(10,5),bp), cell(coord(10,6),e),  cell(coord(10,7),bp), cell(coord(10,8),e),  cell(coord(10,9),e), cell(coord(10,10),e),  cell(coord(10,11),e) ],
        [ cell(coord(11,1),e),  cell(coord(11,2),e),  cell(coord(11,3),e), cell(coord(11,4),e),  cell(coord(11,5),bp), cell(coord(11,6),bp), cell(coord(11,7),bp), cell(coord(11,8),e),  cell(coord(11,9),e), cell(coord(11,10),e),  cell(coord(11,11),e) ]
    ]
).

boardSize(tablut, 9).
initBoard(
    tablut,
    [
        [ cell(coord(1,1),e),   cell(coord(1,2),e),   cell(coord(1,3),e),  cell(coord(1,4),bp),  cell(coord(1,5),bp),  cell(coord(1,6),bp),  cell(coord(1,7),e),   cell(coord(1,8),e),   cell(coord(1,9),e)  ],
        [ cell(coord(2,1),e),   cell(coord(2,2),e),   cell(coord(2,3),e),  cell(coord(2,4),e),   cell(coord(2,5),bp),  cell(coord(2,6),e),   cell(coord(2,7),e),   cell(coord(2,8),e),   cell(coord(2,9),e)  ],
        [ cell(coord(3,1),e),   cell(coord(3,2),e),   cell(coord(3,3),e),  cell(coord(3,4),e),   cell(coord(3,5),wp),  cell(coord(3,6),e),   cell(coord(3,7),e),   cell(coord(3,8),e),   cell(coord(3,9),e)  ],
        [ cell(coord(4,1),bp),  cell(coord(4,2),e),   cell(coord(4,3),e),  cell(coord(4,4),e),   cell(coord(4,5),wp),  cell(coord(4,6),e),   cell(coord(4,7),e),   cell(coord(4,8),e),   cell(coord(4,9),bp) ],
        [ cell(coord(5,1),bp),  cell(coord(5,2),bp),  cell(coord(5,3),wp), cell(coord(5,4),wp),  cell(coord(5,5),wk),  cell(coord(5,6),wp),  cell(coord(5,7),wp),  cell(coord(5,8),bp),  cell(coord(5,9),bp) ],
        [ cell(coord(6,1),bp),  cell(coord(6,2),e),   cell(coord(6,3),e),  cell(coord(6,4),e),   cell(coord(6,5),wp),  cell(coord(6,6),e),   cell(coord(6,7),e),   cell(coord(6,8),e),   cell(coord(6,9),bp) ],
        [ cell(coord(7,1),e),   cell(coord(7,2),e),   cell(coord(7,3),e),  cell(coord(7,4),e),   cell(coord(7,5),wp),  cell(coord(7,6),e),   cell(coord(7,7),e),   cell(coord(7,8),e),   cell(coord(7,9),e)  ],
        [ cell(coord(8,1),e),   cell(coord(8,2),e),   cell(coord(8,3),e),  cell(coord(8,4),e),   cell(coord(8,5),bp),  cell(coord(8,6),e),   cell(coord(8,7),e),   cell(coord(8,8),e),   cell(coord(8,9),e)  ],
        [ cell(coord(9,1),e),   cell(coord(9,2),e),   cell(coord(9,3),e),  cell(coord(9,4),bp),  cell(coord(9,5),bp),  cell(coord(9,6),bp),  cell(coord(9,7),e),   cell(coord(9,8),e),   cell(coord(9,9),e)  ]

    ]
).

boardSize(brandubh, 7).
initBoard(
    brandubh,
    [
        [ cell(coord(1,1),e),   cell(coord(1,2),e),   cell(coord(1,3),e),  cell(coord(1,4),bp),  cell(coord(1,5),e),   cell(coord(1,6),e),   cell(coord(1,7),e)  ],
        [ cell(coord(2,1),e),   cell(coord(2,2),e),   cell(coord(2,3),e),  cell(coord(2,4),bp),  cell(coord(2,5),e),   cell(coord(2,6),e),   cell(coord(2,7),e)  ],
        [ cell(coord(3,1),e),   cell(coord(3,2),e),   cell(coord(3,3),e),  cell(coord(3,4),wp),  cell(coord(3,5),e),   cell(coord(3,6),e),   cell(coord(3,7),e)  ],
        [ cell(coord(4,1),bp),  cell(coord(4,2),bp),  cell(coord(4,3),wp), cell(coord(4,4),wk),  cell(coord(4,5),wp),  cell(coord(4,6),bp),  cell(coord(4,7),bp) ],
        [ cell(coord(5,1),e),   cell(coord(5,2),e),   cell(coord(5,3),e),  cell(coord(5,4),wp),  cell(coord(5,5),e),   cell(coord(5,6),e),   cell(coord(5,7),e)  ],
        [ cell(coord(6,1),e),   cell(coord(6,2),e),   cell(coord(6,3),e),  cell(coord(6,4),bp),  cell(coord(6,5),e),   cell(coord(6,6),e),   cell(coord(6,7),e)  ],
        [ cell(coord(7,1),e),   cell(coord(7,2),e),   cell(coord(7,3),e),  cell(coord(7,4),bp),  cell(coord(7,5),e),   cell(coord(7,6),e),   cell(coord(7,7),e)  ]
    ]
).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%  					         	List Utils			               		  %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%% Puts +Element in last position of +List.
% addLast(+List, +Element, -OutputList)
addLast([], X, [X]).
addLast([X|Xs], Y, [X|L]) :- addLast(Xs, Y, L).

%%% Returns the size of a list.
% [size(+List, -SizeList)]
size([], 0).
size([_|T], M) :- size(T, N), M is N + 1.

%%% Returns a sequence of integers from +N to +M.
% [sequence(+N, +M, -Sequence)]
sequence(N, M, []) :- M < N.
sequence(N, N, [N]).
sequence(N, M, O) :- M > N, M2 is M - 1, sequence(N, M2, T), addLast(T, M, O), !.

%%% Returns if specified lists are equal.
% [equalLists(+List1, +List2)]
equalLists([], []).
equalLists([H|T], [H|T]) :- equalLists(T, T).

%%% Returns a list of the first +N elements of a +List.
% [take(+N, +List, -FirstNElems)]
take(_, [], []).
take(0, _, []).
take(N, [X|Xs], [X|Ys]) :- M is N-1, take(M, Xs, Ys).

%%% Appends four lists in one.
% [append4(+ListOne, +ListTwo, +ListThree, +ListFour, -OutputList)]
append4(L, L1, L2, L3, O) :- append(L, L1, O1), append(L2, L3, O2), append(O1, O2, O).

%%% Returns the +I th element of a +List.
% [ithElem(+I, +List, -ithElement)]
ithElem(I, L, E) :- ithElem(I, L, 1, E).
ithElem(I, [X|Xs], I, X) :- !.
ithElem(I, [X|Xs], C, E) :- C2 is C + 1, ithElem(I, Xs, C2, E).

%%% Sets the +I th element +E of a +List.
% [setIthElem(+E, +I, +List, -OutputList)]
setIthElem(E, I, L, NL) :- setIthElem(E, I, L, 1, NL).
setIthElem(E, I, [X|Xs], I, [E|Xs]) :- !.
setIthElem(E, I, [X|Xs], C, [X|R]) :- C2 is C + 1, setIthElem(E, I, Xs, C2, R).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%  							   Board			                		  %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%% Gets the -Cell located in the specified +Coordinate.
% [getCell(+Board, +Coordinate, -Cell)]
getCell(B, coord(X, Y), C) :- ithElem(X, B, R), ithElem(Y, R, C).

%%% Gets a list of cells located in a list of coordinates.
% [getCellList(+Board, +CoordList, -CellList)]
getCellList(B, [], []).
getCellList(B, [H|T], [O|Os]) :- getCell(B, H, O), getCellList(B, T, Os).

%%% Sets a +Cell in the right location on a +Board.
% [setCell(+Board, +Cell, -OutputBoard)]
setCell(B, cell(coord(X, Y), P), O) :-
		ithElem(X, B, R),
		setIthElem(cell(coord(X, Y), P), Y, R, NR),
		setIthElem(NR, X, B, O).

%%% Sets a list of cells in the right locations on a +Board.
% [setCellList(+Board, +CellList, -OutputBoard)]
setCellList(B, [], B).
setCellList(B, [H|T], O) :- setCell(B, H, NB), setCellList(NB, T, O).

%%% Returns the coordinates of corner cells in a board of the specified +BoardSize.
% [cornerCellCoord(+BoardSize, -Coordinate)]
cornerCellCoord(_, coord(1, 1)).
cornerCellCoord(S, coord(1, S)).
cornerCellCoord(S, coord(S, 1)).
cornerCellCoord(S, coord(S, S)).

%%% Defines the coordinate of the central cell in a board of the specified +BoardSize.
% [centralCellCoord(+BoardSize, -Coordinate))]
centralCellCoord(S, coord(X, X)) :- X is (S // 2) + 1.

%%% Returns all corner cells of spiecified +BoardSize.
% [allCornerCells(+BoardSize, -ListCornerCells)]
allCornerCells(S, L) :- findall(C, cornerCellCoord(S, C), L).

%%% Returns corner cells and center cell in a board of the specified +BoardSize.
% [specialCellsCoords(+BoardSize, -ListSpecialCells)]
specialCellsCoords(S, O) :- allCornerCells(S, C), centralCellCoord(S, C1), append(C, [C1], O).

%%% Returns if cell lcoated in +Coordinate is a special cell in a board
%%% of the specified +BoardSize.
% [isSpecialCell(+BoardSize, +Coordinate)]
isSpecialCell(S, C) :- specialCellsCoords(S, L), member(C, L).

%%% Returns the list of the coordinates next to the central cell.
% [nextToCentralCellCoords(+BoardSize, -CoordinateList)]
nextToCentralCellCoords(S, [U, R, D, L]) :-
		upCentralCellCoord(S, U),
		rightCentralCellCoord(S, R),
		downCentralCellCoord(S, D),
		leftCentralCellCoord(S, L).
upCentralCellCoord(S, coord(X, Y)) :- X is (S // 2), Y is (S // 2) + 1.
rightCentralCellCoord(S, coord(X, Y)) :- X is (S // 2) + 1, Y is (S // 2) + 2.
downCentralCellCoord(S, coord(X, Y)) :- rightCentralCellCoord(S, coord(Y, X)).
leftCentralCellCoord(S, coord(X, Y)) :- upCentralCellCoord(S, coord(Y, X)).

%%% Returns the -PlayerOwner of the piece located in the specified +Cell.
% [playerOwner(+Cell, -PlayerOwner)]
cellOwner(cell(_, P), O) :- pieceOwner(P, O).

%%% Returns the -PlayerOwner of a +Piece.
% [pieceOwner(+Piece, -PlayerOwner)]
pieceOwner(wp, w).
pieceOwner(wk, w).
pieceOwner(bp, b).

%%% Returns if a +Piece is a pawn.
% isPawn(+Piece)
isPawn(bp).
isPawn(wp).

%%% Returns all the orthogonal cells of the specified +Coordinate in the given +Board,
%%% divided in all four directions and ordered from closest to farthest.
% [orthogonalCells(+BoardSize, +Board, +Coordinate, -UpCells, -RightCells, -DownCells, -LeftCells)]
orthogonalCells(S, B, C, U, R, D, L) :-
		upCells(B, C, U),
		rightCells(S, B, C, R),
		downCells(S, B, C, D),
		leftCells(B, C, L).

%%% Returns all the cells above the specified +Coordinate in the given +Board,
%%% ordered from closest to farthest.
% [upCells(+Board, +Coord, -UpCells)]
upCells(B, coord(FromX, FromY), U) :-
		% (X, FromY) coordinates where X < FromX
		PrevX is FromX - 1,
		sequence(1, PrevX, Xs),
		findall(coord(X, FromY), member(X, Xs), Coords),
		getCellList(B, Coords, U1),
		% ordering them from nearest to closest to +Coord
		reverse(U1, U).

%%% Returns all the cells on the right of the specified +Coordinate in the given +Board,
%%% ordered from closest to farthest.
% [rightCells(+BoardSize, +Board, +Coord, -LeftCells)]
rightCells(S, B, coord(FromX, FromY), R) :-
		% taking last S - FromY elements from the FromX row
		NToTake is S - FromY,
		ithElem(FromX, B, Row),
		reverse(Row, ReverseRow),
		take(NToTake, ReverseRow, ReverseR),
		reverse(ReverseR, R).

%%% Returns all the cells below the specified +Coordinate in the given +Board,
%%% ordered from closest to farthest.
% [downCells(+BoardSize, +Board, +Coord, -DownCells)]
downCells(S, B, coord(FromX, FromY), D) :-
		% (X, FromY) coordinates where X > FromX
		NextX is FromX + 1,
		sequence(NextX, S, Xs),
		findall(coord(X, FromY), member(X, Xs), Coords),
		getCellList(B, Coords, D).

%%% Returns all the cells on the left of the specified +Coordinate in the given +Board,
%%% ordered from closest to farthest.
% [leftCells(+Board, +Coord, -LeftCells)]
leftCells(B, coord(FromX, FromY), L) :-
		% taking first FromY - 1 elements from the FromX row
		NToTake is FromY - 1,
		ithElem(FromX, B, Row),
		take(NToTake, Row, L1),
		% ordering them from nearest to closest to +Coord
		reverse(L1, L).

%%% Returns the first +N adjacent cells to specified +Coordinate cell,
%%% divided in all four directions and ordered from closest to farthest.
% [getNAdjacentCells(+BoardSize, +Board, +N, +Coordinate, -Up, -Right, -Down, -Left)]
getNAdjacentCells(S, B, N, C, U, R, D, L) :-
		orthogonalCells(S, B, C, U1, R1, D1, L1),
		take(N, U1, U),
		take(N, R1, R),
		take(N, D1, D),
		take(N, L1, L), !.

%%% Checks if each cell of a list contains a black pawn.
% [allBlackPawns(+CellsList)]
allBlackPawns([cell(_, bp)]).
allBlackPawns([cell(_, bp)|T]) :- allBlackPawns(T).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%  				                  Game			                	      %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%% Defines the opponent in the game (w: white, b: black).
% [opponent(+Player, -Opponent)]
opponent(w, b).
opponent(b, w).

%%% Returns the next player to move in the game.
% [nextPlayerToMove(+Previous, -Next)]
nextPlayerToMove(X, Y) :- opponent(X, Y).

%%% Inits a new game of the specified game variant
%%% Game ex: (Variant, PlayerToMove, Winner, Board)
%%% (Winner = n: none, w: white, b: black, d: draw)
% [newGame(+Variant, -Game)]
newGame(V, (V, b, n, B)) :- initBoard(V, B).

%%% Returns the -Board of the specified +Game.
% [gameBoard(+Game, -Board)]
gameBoard((_, _, _, B), B).

%%% Returns the -Winner of the specified +Game.
% [gameWinner(+Game, -Winner)
gameWinner((_, _, W, _), W).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%  				              Possible Moves		                      %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%% Returns all the possible moves in a specified Game at the current state.
%%% (NB: only for PlayerToMove pieces, see Game example above).
%%% ListOfPossibleMoves ex.: [(FromCoord, ToCoord), ...]
% [gamePossibleMoves(+Game, -ListOfPossibleMoves)]
gamePossibleMoves(G, O) :- gameBoard(G, B), computeMoves(G, B, O).

% [computeMoves(+Game, +RowsToInspect, -ListOfPossibleMoves)
computeMoves(_, [], []).
computeMoves(G, [R|Rs], O) :-
		computeMovesForRowCells(G, R, RO),
		computeMoves(G, Rs, O1),
		append(RO, O1, O).
%%% Browses +CellsToInspect and if a cell contains a PlayerToMove piece computes
%%% all the possible moves for that piece:
% [computeMoves(+Game, +CellsToInspect, -ListOfPossibleMoves)
computeMovesForRowCells((_, _, _, _), [], []).
computeMovesForRowCells((V, P, W, B), [C|T], O) :-
		cellOwner(C, P),
		possibleMoves((V, P, W, B), C, M),
		computeMovesForRowCells((V, P, W, B), T, M1),
		append(M, M1, O), !.
% Other cases: opponent Player or piece(void)
computeMovesForRowCells(G, [H|T], O) :- computeMovesForRowCells(G, T, O).

%%% Checks if there is at least one possible move available in the specified game.
% [findAnyMove(+Game, +RowsToInspect)
findAnyMove(G, [R|Rs]) :-
		(findAnyMoveRow(G, R) ; findAnyMove(G, Rs)), !.
%%% Checks if there is at least one possible move available in the specified game
%%% in the specified row.
% [findAnyMove(+Game, +CellsToInspect)
findAnyMoveRow((V, P, W, B), [C|T]) :-
		cellOwner(C, P),
		possibleMoves((V, P, W, B), C, M),
		((size(M, S), S > 0) ; findAnyMoveRow((V, P, W, B), T)), !.
findAnyMoveRow(G, [_|T]) :- findAnyMoveRow(G, T).

%%% Returns all possible moves of the specified coordinate in the specified Game
%%% (NB: only for PlayerToMove pieces, see Game example above).
%%% ListOfPossibleMoves ex.: [(ToCoord), ...]
% [getCoordPossibleMoves(+Game, +FromCoord, -ListOfPossibleMoves)]
getCoordPossibleMoves((V, P, n, B), FromCoord, O) :-
		getCell(B, FromCoord, C),
		cellOwner(C, P),
		possibleMoves((V, P, W, B), C, M),
		only_ToMove(M, O), !.
% Other cases: non player to move piece, empty or game has ended
getCoordPossibleMoves((_, _, _, _), _, []).

%%% Maps a list of moves expressed like (FromCoordinate, ToCoordinate) as (ToCoordinate).
% only_ToMove(+ListOfMoves, -MappedList)
only_ToMove([], []).
only_ToMove([(_, ToCoord)|T], [ToCoord|T1]) :- only_ToMove(T, T1).

%%% Returns all possible moves of the piece in the specified cell.
%%% (NB: assumes that +Cell contains a PlayerToMove piece, if you need to check piece
%%% player you may want to use getCoordPossibleMoves!)
% [possibleMoves(+Game, +Cell, -ListOfPossibleMoves)]
possibleMoves((V, _, _, B), cell(FromCoord, P), O) :-
		boardSize(V, S),
		orthogonalCells(S, B, FromCoord, U, R, D, L),
		cutAfterPieces(U, R, D, L, FromCoord, M),
		filterIfPawn(P, S, M, O).

%%% Cuts the last cells in a specified sequence of cells as soon as it founds one
%%% containing a piece; maps them as moves like (FromCoordinate, ToCoordinate).
% [cut(+SequenceOfCells, +FromCoordinate, -LineMoves)]
cut([], FromCoord, []).
cut([cell(ToCoord, e)|T], FromCoord, [(FromCoord, ToCoord)|O]) :-
		cut(T, FromCoord, O).
% Cuts as soon as it founds a piece
cut([cell(coord(_, _), P)|_], _, []) :- P \= e, !.

%%% Cuts four sequences of cells (one for each direction) as soons as it founds
%%% in each one a cell containing a piece; maps them as moves like (FromCoordinate, ToCoordinate).
% [cutAfterPieces(+UpCells, +RightCells, +DownCells, +LeftCells, +FromCoordinate, -MovesList)]
cutAfterPieces(U, R, D, L, FromCoord, O) :-
		cut(U, FromCoord, U1),
		cut(R, FromCoord, R1),
		cut(D, FromCoord, D1),
		cut(L, FromCoord, L1),
		append4(U1, R1, D1, L1, O).

%%% Filters a list of moves for a piece removing special cells (corners and center) if the piece is a pawn.
% [filterIfPawn(+PieceType, +BoardSize, +ListOfMoves, -FilteredMovesList)]
filterIfPawn(bp, S, L, O) :- filterSpecialCells(S, L, O).
filterIfPawn(wp, S, L, O) :- filterSpecialCells(S, L, O).
filterIfPawn(wk, _, L, L).

%%% Filters a list of moves removing special cells (corners and center).
% [filterSpecialCells(+BoardSize, +MovesList, -FilteredMovesList)]
filterSpecialCells(S, L, O) :- specialCellsCoords(S, SpCells), removeSpecialCells(L, SpCells, O).
removeSpecialCells([], SpCells, []).
removeSpecialCells([(_, ToCoord)|T], SpCells, O) :-
		member(ToCoord, SpCells),
		removeSpecialCells(T, SpCells, O), !.
removeSpecialCells([(FromCoord, ToCoord)|T], SpCells, [(FromCoord, ToCoord)|T1]) :-
		not(member(ToCoord, SpCells)),
		removeSpecialCells(T, SpCells, T1).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%  						     Making Moves 			            		  %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%% Makes a move from +FromCoordinate to +ToCoordinate in a specified +Game,
%%% the move must be legit.
% [makeLegitMove(+Game, +FromCoordinate, +ToCoordinate, -NumberOfCaptures, -UpdatedGame)]
makeLegitMove(G, FromCoord, ToCoord, NC, O) :-
		checkLegitMove(G, FromCoord, ToCoord),
		makeMove(G, FromCoord, ToCoord, NC, O), !.

%%% Checks if the move from +FromCoordinate to +ToCoordinate in a specified +Game is legit.
% [checkLegitMove(+Game, +FromCoordinate, +ToCoordinate)]
checkLegitMove(G, FromCoord, ToCoord) :-
		getCoordPossibleMoves(G, FromCoord, M),
		member((ToCoord), M).

%%% Makes a move from +FromCoordinate to +ToCoordinate in a specified +Game.
%%% NB: assumes that the move is legit!
% [makeMove(+Game, +FromCoordinate, +ToCoordinate, -NumberOfCaptures, -UpdatedGame)]
makeMove((V, P, _, B), FromCoord, ToCoord, NC, (V, O, W, NB)) :-
		nextPlayerToMove(P, O),
		boardSize(V, S),
		move(S, B, FromCoord, ToCoord, NC, NB),
		winner(V, NB, P, ToCoord, W).

%%% Moves the piece located in +FromCoordinate to +ToCoordinate and performs captures.
% [move(+BoardSize, +Board, +FromCoordinate, +ToCoordinate, -NumberOfCaptures, -OutputBoard)]
move(S, B, FromCoord, ToCoord, NC, O) :-
		getCell(B, FromCoord, cell(FromCoord, P)),
		writeMove(B, P, FromCoord, ToCoord, NB),
		pieceOwner(P, Player),
		checkAllCaptures(S, NB, Player, ToCoord, LC),
		size(LC, NC),
		mapCoordsToCapturedCells(LC, CC),
		setCellList(NB, CC, O), !.

%%% Writes a move from +FromCoordinate to +ToCoordinate in the specified +Board:
%%% sets +Piece in ToCoordinate and sets empty in +FromCoordinate.
% [writeMove(+Board, +Piece, +FromCoordinate, +ToCoordinate, -NewBoard)]
writeMove(B, P, FromCoord, ToCoord, NB) :-
		setCell(B, cell(ToCoord, P), B1),
		setCell(B1, cell(FromCoord, e), NB).

%%% Returns the coordinates of the captured pawn on a side,
%%% returns an empty list if there are no captures.
% [checkCapture(+Size, +PlayerWhoMoved, +AdjacentCells, -CapturesList)]
checkCapture(_, _, [], []).
checkCapture(_, _, [X], []).
checkCapture(_, _, [cell(_, e)|_], []).
checkCapture(_, Player, [cell(_, Piece)|_], []) :-
		pieceOwner(Piece, Player).
checkCapture(_, Player, [cell(_, Piece),cell(_, Piece1)], []) :-
		pieceOwner(Piece, Opponent),
		pieceOwner(Piece1, Opponent).
% King captured triggers the end of the game so it is evaluated in checkVictory,
% moreover, the rule for capturing the king is different
checkCapture(_, Player, [cell(_, wk),_], []).
checkCapture(_, Player, [cell(C, Piece),cell(C1, Piece1)], [C]) :-
		pieceOwner(Piece, Opponent),
		pieceOwner(Piece1, Player),
		isPawn(Piece).
checkCapture(S, Player, [cell(C, Piece),cell(C1, e)], [C]) :-
		pieceOwner(Piece, Opponent),
		isPawn(Piece),
		isSpecialCell(S, C1), !.
checkCapture(S, Player, [cell(C, Piece),cell(C1, e)], []) :-
		pieceOwner(Piece, Opponent),
		not(isSpecialCell(S, C1)).

%%% Returns all captured pieces coordinates after +PlayerWhoMoved moved a piece to +MovedCoord,
%%% returns an empty list if there are no captures.
% [checkAllCaptures(+BoardSize, +Board, +PlayerWhoMoved, +MovedCoord, -CapturesList)]
checkAllCaptures(S, B, P, MovedCoord, C) :-
		getNAdjacentCells(S, B, 2, MovedCoord, U, R, D, L),
		checkCapture(S, P, U, UC),
		checkCapture(S, P, R, RC),
		checkCapture(S, P, D, DC),
		checkCapture(S, P, L, LC),
		append4(UC, RC, DC, LC, C), !.

%%% Maps a list of coordinates to a list of empty cells.
% [mapCoordsToCapturedCells(+ListOfCoords, -ListOfEmptyCells)]
mapCoordsToCapturedCells([], []).
mapCoordsToCapturedCells([H|T], [cell(H, e)|T1]) :- mapCoordsToCapturedCells(T, T1).

%%% Returns the winner of the game specified with +GameVariant, current +Board,
%%% last +PlayerWhoMoved and last move arriving +MoveCoordinate.
% [winner(+GameVariant, +Board, +PlayerWhoMoved, +MoveCoordinate, -Winner)]
winner(V, B, P, MoveCoord, P) :- checkVictory(V, B, P, MoveCoord), !.
winner(V, B, P, _, d) :- nextPlayerToMove(P, O), checkDraw((V, O, n, B)), !.
winner(_, _, _, _, n).

%%% Checks if the player who moved has won.
% [checkVictory(+Variant, +Board, +Winner, +LastMoveCoordinate)]
% (only the white king can move to a corner cell -> no need to find king's coordinate)
checkVictory(V, B, w, MoveCoord) :- boardSize(V, S), allCornerCells(S, L), member(MoveCoord, L), !.
% (king capture is different according to game variant)
checkVictory(V, B, b, MoveCoord) :-
		(V == hnefatafl ; V == tawlbwrdd),
		boardSize(V, S),
		checkBlackBigBoardVictory(S, B, MoveCoord), !.
checkVictory(V, B, b, MoveCoord) :-
		(V == tablut ; V == brandubh),
		boardSize(V, S),
		checkBlackSmallBoardVictory(S, B, MoveCoord).

%%% Checks if the game is drawn.
% [checkDraw(+Game)]
checkDraw(G) :- not(onePossibleMoveIsAvalaible(G)).
onePossibleMoveIsAvalaible(G) :- gameBoard(G, B), findAnyMove(G, B).

%%% Finds the king's coordinate in the specified +Board.
% [findKing(+Board, -KingCoordinate)]
findKing([[]|Rs], C) :- findKing(Rs, C).
findKing([[cell(C, wk)|T]|Rs], C) :- !.
findKing([[_|T]|Rs], C) :- findKing([T|Rs], C).

%%% Returns if the king is on throne (central cell).
% [kingOnThrone(+BoardSize, +KingCoordinate)]
kingOnThrone(S, C) :- centralCellCoord(S, C).

%%% Returns if the king is on a cell next to the throne (central cell).
% [kingNextToThrone(+BoardSize, +KingCoordinate)]
kingNextToThrone(S, C) :-
		nextToCentralCellCoords(S, [U, R, D, L]),
		append4(U, R, D, L, Around),
		member(C, Around).

%%% Returns king's adjacent cells divided in Horizontal and Vertical (one cell per side).
% [kingAdjacentCells(+BoardSize, +Board, +KingCoordinate, -HorizontalAdjacentCells, -VerticalAdjacentCells)]
kingAdjacentCells(S, B, C, Horiz, Vert) :-
		getNAdjacentCells(S, B, 1, C, U, R, D, L),
		append(U, D, Vert),
		append(R, L, Horiz).

%%% Checks if black won in a game variant with big board
% [checkBlackBigBoardVictory(+BoardSize, +Booard, +LastMoveCoordinate)]
checkBlackBigBoardVictory(S, B, MoveCoord) :-
		findKing(B, C),
		kingAdjacentCells(S, B, C, H, V),
		(kingCapturedFourSides(H, V) ; kingCapturedThreeSidesAndThrone(S, H, V, MoveCoord)).

%%% Checks if black won in a game variant with small board.
% [checkBlackBigBoardVictory(+BoardSize, +Booard, +LastMoveCoordinate)]
checkBlackSmallBoardVictory(S, B, MoveCoord) :-
		findKing(B, C),
		kingAdjacentCells(S, B, C, H, V),
		checkKingCapturedSmallBoard(S, C, H, V, MoveCoord).

%%% Checks all different ways the king could have been captured in a small board
%%% game variant.
% [checkKingCapturedSmallBoard(+BoardSize, +KingCoordinate, +KingHorizAdjacentCells, +KingVertAdjacentCells, +LastMoveCoordinate)]
checkKingCapturedSmallBoard(S, C, H, V, _) :-
		kingOnThrone(S, C),
		kingCapturedFourSides(H, V), !.
checkKingCapturedSmallBoard(S, C, H, V, MoveCoord) :-
		kingNextToThrone(S, C),
		kingCapturedThreeSidesAndThrone(S, H, V, MoveCoord), !.
checkKingCapturedSmallBoard(S, C, H, V, MoveCoord) :-
		not(kingOnThrone(S, C)),
		not(kingNextToThrone(S, C)),
		kingCapturedTwoSides(H, V, MoveCoord).

%%% Checks if the king has been captured surrounded on four sides.
% [kingCapturedFourSides(+KingHorizAdjacentCells, +KingVertAdjacentCells)]
kingCapturedFourSides(V, H) :-
		append(V, H, Around),
		allBlackPawns(Around), !.

%%% Checks if the king has been captured surrounded on three sides and
%%% hostile central cell in the last one.
% [kingCapturedThreeSidesAndThrone(+BoardSize, +KingHorizAdjacentCells, +KingVertAdjacentCells, +LastMoveCoordinate)]
kingCapturedThreeSidesAndThrone(S, H, V, MoveCoord) :-
		append(H, V, Around),
		member(cell(MoveCoord, _), Around),
		centralCellCoord(S, Central),
		delete(cell(Central, _), Around, FilteredAround),
		allBlackPawns(FilteredAround), !.

%%% Checks if the king has been captured surrounded on two sides.
% [kingCapturedTwoSides(+KingHorizAdjacentCells, +KingVertAdjacentCells, +LastMoveCoordinate)]
kingCapturedTwoSides(H, V, MoveCoord) :-
		(kingCapturedInLine(MoveCoord, H) ; kingCapturedInLine(MoveCoord, V)).
%%% Checks if the king has been captured surrounded on two sides in specified line.
% [kingCapturedTwoSides(+KingHorizAdjacentCells, +KingLineAdjacentCells)]
kingCapturedInLine(MoveCoord, LineSides) :-
		member(cell(MoveCoord, _), LineSides),
		size(LineSides, S),
		S > 1, % can't be captured on edge of the board
		allBlackPawns(LineSides).





%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%  					       List Utils Tests		          		     	  %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


testingList([1,2,3,4,5,6,7,8,9]).

% yes
testAddLast :- testingList(L), addLast(L, 10, [1,2,3,4,5,6,7,8,9,10]).

% yes
testSize :- testingList(L), size(L, 9).

% yes
testSequence :- sequence(4, 7, [4,5,6,7]).
% yes
testSequenceBiggerM :- sequence(7, 4, []).

% yes
testEqualLists :- testingList(L), equalLists(L, [1,2,3,4,5,6,7,8,9]).
% no
testDifferentLists :- testingList(L), equalLists(L, [1,2,3,4]).

% yes
testTake :- testingList(L), take(3, L, [1,2,3]).
% yes
testTakeBiggerN :- testingList(L), size(L, S), N is S + 5, take(N, L, [1,2,3,4,5,6,7,8,9]).

% yes
testAppend4 :- append4([1,2,3],[4,5,6],[7,8,9],[10,11,12], [1,2,3,4,5,6,7,8,9,10,11,12]).
% yes
testAppend4Empty :- append4([],[],[],[], []).
% yes
testAppend4SomeEmpty :- append4([],[4,5,6],[],[10,11,12], [4,5,6,10,11,12]).

% yes
testIthElem :- testingList(L), ithElem(4, L, 4).
% no
testOutIthElem :- testingList(L), ithElem(10, L, O).

% yes
testSetIthElem :- testingList(L), setIthElem(elem, 4, L, [1,2,3,elem,5,6,7,8,9]).
% no
testSetOutIthElem :- testingList(L), setIthElem(elem, 10, L, O).

% yes
allTestsLists :-
		testAddLast,
		testSize,
		testSequence,
		testSequenceBiggerM,
		testEqualLists,
		not(testDifferentLists),
		testTake,
		testTakeBiggerN,
		testAppend4,
		testAppend4Empty,
		testAppend4SomeEmpty,
		testIthElem,
		not(testOutIthElem),
		testSetIthElem,
		not(testSetOutIthElem), !.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%  						    	Board Tests	   	               		      %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


% yes
testGetCell :-
		initBoard(hnefatafl, B),
		getCell(B, coord(6,6), cell(coord(6,6),wk)).
% yes
testGetCell1 :-
		initBoard(hnefatafl, B),
		getCell(B, coord(1,11), cell(coord(1,11),e)).
% no
testGetOutCell :-
		initBoard(hnefatafl, B),
		getCell(B, coord(1,12), C).

% yes
coordOne(coord(3,6)).
coordTwo(coord(7,8)).
testGetCellList :-
		initBoard(hnefatafl, B),
		coordOne(C), coordTwo(C1),
		getCellList(B, [C,C1], O),
		getCell(B, C, Cell1), getCell(B, C1, Cell2),
		equalLists(O, [Cell1,Cell2]).

% yes
cellOne(cell(C,elem)) :- coordOne(C).
testSetCell :-
		initBoard(hnefatafl, B),
		cellOne(C),
		coordOne(Coord),
		setCell(B, C, NB),
		getCell(NB, Coord, C).
% no
testSetOutCell :-
		initBoard(hnefatafl, B),
		setCell(B, cell(coord(1, 12)), NB).

% yes
cellTwo(cell(C,elem)) :- coordTwo(C).
testSetCellList :-
		initBoard(hnefatafl, B),
		cellOne(C), cellTwo(C1),
		coordOne(Coord), coordTwo(Coord1),
		setCellList(B, [C,C1], NB),
		getCellList(NB, [Coord,Coord1], [C,C1]).

% yes
testSpecialCellsBoard7 :-
		boardSize(brandubh, S),
		specialCellsCoords(S, [coord(1,1),coord(1,7),coord(7,1),coord(7,7),coord(4,4)]).
% yes
testSpecialCellsBoard11 :-
		boardSize(tawlbwrdd, S),
		specialCellsCoords(S, [coord(1,1),coord(1,11),coord(11,1),coord(11,11),coord(6,6)]).
% yes
testIsSpecialCell :-
		boardSize(tawlbwrdd, S),
		specialCellsCoords(S, [H|T]),
		isSpecialCell(S, H).

% yes
testCellOwner :-
		cellOwner(cell(fakeCoord,wp), w).
% yes
testCellOwner1 :-
		cellOwner(cell(fakeCoord,wk), w).
% no
testCellOwner2 :-
		cellOwner(cell(fakeCoord,bp), w).
% yes
testCellOwner3 :-
		cellOwner(cell(fakeCoord,bp), b).

% ( coordOne = coord(3,6) )
% yes
testOrthogonalCells :-
		initBoard(brandubh, B),
		boardSize(brandubh, S),
		coordOne(C),
		orthogonalCells(S, B, C,
				[cell(coord(2,6),e),cell(coord(1,6),e)],
				[cell(coord(3,7),e)],
				[cell(coord(4,6),bp),cell(coord(5,6),e),cell(coord(6,6),e),cell(coord(7,6),e)],
				[cell(coord(3,5),e),cell(coord(3,4),wp),cell(coord(3,3),e),cell(coord(3,2),e),cell(coord(3,1),e)]).

% yes
nTest(3).
testNAdjacentCells :-
		initBoard(brandubh, B),
		boardSize(brandubh, S),
		coordOne(C),
		nTest(N),
		getNAdjacentCells(S, B, N, C, U, R, D, L),
		size(U, SU), SU =< N,
		size(R, RU), RU =< N,
		size(D, DU), DU =< N,
		size(L, LU), LU =< N.

% yes
testAllBlackPawns :- allBlackPawns([cell(c,bp),cell(c,bp)]).
% no
testAllBlackPawns1 :- allBlackPawns([cell(c,wp),cell(c,bp)]).
% no
testAllBlackPawns2 :- allBlackPawns([cell(c,bp),cell(c,e)]).

% yes
allTestsBoard :-
		testGetCell,
		testGetCell1,
		not(testGetOutCell),
		testGetCellList,
		testSetCell,
		not(testSetOutCell),
		testSetCellList,
		testSpecialCellsBoard7,
		testSpecialCellsBoard11,
		testIsSpecialCell,
		testCellOwner,
		testCellOwner1,
		not(testCellOwner2),
		testCellOwner3,
		testOrthogonalCells,
		testNAdjacentCells,
		testAllBlackPawns,
		not(testAllBlackPawns1),
		not(testAllBlackPawns2), !.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%  						Possible Moves Tests	                          %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


% yes
testGamePossibleMoves :-
		newGame(brandubh, G),
		gamePossibleMoves(G, M),
		size(M, 40).
% yes
testGamePossibleMoves1 :-
		newGame(hnefatafl, G),
		gamePossibleMoves(G, M),
		size(M, 116).

% yes
testGetCoordPossibleMoves :-
		newGame(tablut, G),
		getCoordPossibleMoves(G, coord(1,4), [coord(2,4),coord(3,4),coord(4,4),coord(1,3),coord(1,2)]).
% yes
testGetCoordPossibleMovesEmptyCell :-
		newGame(hnefatafl, G),
		getCoordPossibleMoves(G, coord(1,1), []).
% yes
testGetCoordPossibleMovesNotPlayerToMove :-
		newGame(tawlbwrdd, G),
		getCoordPossibleMoves(G, coord(4,6), []).
% yes
testGetCoordPossibleMovesBlockedPiece :-
		newGame(hnefatafl, G),
		getCoordPossibleMoves(G, coord(6,11), []).

% yes
testGetCoordPossibleMovesKingSpecialCells :-
		newGame(brandubh, (V, _, _, B)),
		setEmptyBoard(B, NB),
		setCell(NB, cell(coord(1,4),wk), NB1),
		getCoordPossibleMoves((V, w, n, NB1), coord(1,4),
		[coord(1,5),coord(1,6),coord(1,7),coord(2,4),coord(3,4),coord(4,4),coord(5,4),
		coord(6,4),coord(7,4),coord(1,3),coord(1,2),coord(1,1)]).
setEmptyBoard([], []).
setEmptyBoard([R|Rs], [NR|O]) :- setEmptyRow(R,NR), setEmptyBoard(Rs,O).
setEmptyRow([], []).
setEmptyRow([cell(C,_)|T],[cell(C,e)|T1]) :- setEmptyRow(T, T1).

% yes
testFindAnyMove :- newGame(tablut, G), gameBoard(G, B), findAnyMove(G, B).
% yes
testFindNoMove :- newGame(hnefatafl, G), gameBoard(G, B), setEmptyBoard(B, EB), not(findAnyMove(G, EB)).

% yes
allTestsPossibleMoves :-
		testGamePossibleMoves,
		testGamePossibleMoves1,
		testGetCoordPossibleMoves,
		testGetCoordPossibleMovesEmptyCell,
		testGetCoordPossibleMovesNotPlayerToMove,
		testGetCoordPossibleMovesBlockedPiece,
		testGetCoordPossibleMovesKingSpecialCells,
		testFindAnyMove,
		testFindNoMove.


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%  						  Making Moves Tests	                          %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


% yes.
testHorizCapture :-
		newGame(tablut, G),
		makeMove(G, coord(9,6), coord(6,6), _, G1),
		% one capture NC / 1
		makeMove(G1, coord(5,7), coord(6,7), 1, G2),
		gameBoard(G2, B),
		% captured cell set empty
		getCell(B, coord(6,6), cell(coord(6,6),e)).

% yes.
testVertCapture :-
		newGame(tawlbwrdd, G),
		makeLegitMove(G, coord(5,10), coord(5,8), _, G1),
		% one capture NC / 1
		makeLegitMove(G1, coord(4,6), coord(4,8), 1, G2),
		gameBoard(G2, B),
		% captured cell set empty
		getCell(B, coord(5,8), cell(coord(5,8),e)).

% yes.
testDoubleCapture :-
		newGame(hnefatafl, G),
		makeMove(G, coord(1, 4), coord(4, 4), _, G1),
		makeMove(G1, coord(4, 6), coord(4, 5), _, G2),
		makeMove(G2, coord(1, 5), coord(3, 5), _, G3),
		makeMove(G3, coord(5, 5), coord(5, 4), _, G4),
		makeMove(G4, coord(5, 1), coord(5, 3), _, G5),
		% two captures NC / 2
		makeMove(G5, coord(5, 11), coord(5, 5), 2, O),
		gameBoard(O, B),
		% captured cells set empty
		getCell(B, coord(4, 5), cell(coord(4,5),e)),
		getCell(B, coord(5, 4), cell(coord(5,4),e)), !.

% yes.
testTripleCapture :-
		newGame(tawlbwrdd, G),
		makeMove(G, coord(1, 5), coord(4, 4), _, G1),
		makeMove(G1, coord(1, 6), coord(4, 8), _, G2),
		makeMove(G2, coord(4, 6), coord(4, 5), _, G3),
		makeMove(G3, coord(6, 6), coord(1, 2), _, G4),
		makeMove(G4, coord(5, 7), coord(4, 7), _, G5),
		makeMove(G5, coord(11, 5), coord(11, 4), _, G6),
		% three captures NC / 3
		makeMove(G6, coord(3, 6), coord(4, 6), 3, O),
		gameBoard(O, B),
		% captured cells set empty
		getCellList(B, [coord(4,5),coord(4,7),coord(5,6)],
				[cell(coord(4,5),e),cell(coord(4,7),e),cell(coord(5,6),e)]), !.

% yes
testWhiteWin :-
		newGame(hnefatafl, G),
		makeMove(G, coord(1, 4), coord(1, 3), _, G1),
		makeMove(G1, coord(6, 6), coord(1, 1), _, O),
		gameWinner(O, w).

% yes
testWhiteWin1 :-
		newGame(hnefatafl, G),
		makeMove(G, coord(1, 4), coord(1, 3), _, G1),
		makeMove(G1, coord(6, 6), coord(1, 11), _, O),
		gameWinner(O, w).

% yes
testBoard11KingCaptured3SidesAndThrone :-
		newGame(hnefatafl, G),
		makeMove(G, coord(1, 4), coord(5, 5), _, G1),
		makeMove(G1, coord(6, 6), coord(6, 5), _, G2),
		makeMove(G2, coord(1, 5), coord(6, 4), _, G3),
		makeMove(G3, coord(4, 6), coord(4, 2), _, G4),
		makeMove(G4, coord(1, 6), coord(7, 5), _, O),
		gameWinner(O, b).

% yes
testBoard11KingNotCaptured3Sides :-
		newGame(tawlbwrdd, G),
		makeMove(G, coord(1, 5), coord(2, 3), _, G1),
		makeMove(G1, coord(6, 6), coord(3, 3), _, G2),
		makeMove(G2, coord(1, 6), coord(3, 2), _, G3),
		makeMove(G3, coord(4, 6), coord(4, 11), _, G4),
		makeMove(G4, coord(1, 7), coord(4, 3), _, O),
		gameWinner(O, n).

% yes
testBoard11KingNotCapturedOnEdge :-
		newGame(hnefatafl, G),
		makeMove(G, coord(10, 6), coord(10, 11), _, G1),
		makeMove(G1, coord(6, 6), coord(9, 11), _, G2),
		makeMove(G2, coord(6, 10), coord(9, 10), _, O),
		gameWinner(O, n).

% yes
testBoard11KingOnThroneCapture :-
		newGame(hnefatafl, G),
		makeMove(G, coord(1, 4), coord(5, 6), NC1, G1),
		makeMove(G1, coord(1, 5), coord(6, 5), NC2, G2),
		makeMove(G2, coord(1, 6), coord(6, 7), NC3, G3),
		makeMove(G3, coord(4, 6), coord(4, 2), NC4, G4),
		makeMove(G4, coord(1, 7), coord(7, 6), NC5, O),
		gameWinner(O, b).

% yes
testBoard11KingFarFromThroneCapture :-
		newGame(hnefatafl, G),
		makeMove(G, coord(1, 4), coord(3, 3), NC1, G1),
		makeMove(G1, coord(1, 5), coord(4, 2), NC2, G2),
		makeMove(G2, coord(1, 6), coord(4, 4), NC3, G3),
	  makeMove(G3, coord(6, 6), coord(4, 3), NC4, G4),
		makeMove(G4, coord(1, 7), coord(5, 3), NC5, O),
		gameWinner(O, b).

% yes
testBoard9KingOnThroneCapture4Sides :-
		newGame(tablut, G),
		makeMove(G, coord(2, 5), coord(6, 5), _, G1),
		makeMove(G1, coord(1, 4), coord(4, 5), _, G2),
		makeMove(G2, coord(1, 6), coord(5, 6), _, G3),
		makeMove(G3, coord(3, 5), coord(3, 4), _, G4),
		makeMove(G4, coord(1, 5), coord(5, 4), _, O),
		gameWinner(O, b).

% yes
testBoard7KingOnThroneNoCapture3Sides :-
		newGame(brandubh, G),
		makeMove(G, coord(1, 4), coord(3, 4), _, G1),
		makeMove(G1, coord(2, 4), coord(4, 3), _, G2),
		makeMove(G2, coord(4, 1), coord(4, 5), _, O),
		gameWinner(O, n).

% yes
testBoard9KingFarFromThroneVertCapture :-
		newGame(tablut, G),
		makeMove(G, coord(1, 4), coord(1, 3), _, G1),
		makeMove(G1, coord(5, 5), coord(2, 3), _, G2),
		makeMove(G2, coord(1, 5), coord(3, 3), _, O),
		gameWinner(O, b).

% yes
testBoard9KingFarFromThroneHorizCapture :-
		newGame(tablut, G),
		makeMove(G, coord(6, 9), coord(8, 9), _, G1),
		makeMove(G1, coord(5, 5), coord(8, 8), _, G2),
		makeMove(G2, coord(8, 5), coord(8, 7), _, O),
		gameWinner(O, b).

% yes
testBoard9SneakyKingNotCaptured :-
		newGame(tablut, G),
		makeMove(G, coord(6, 9), coord(7, 9), _, G1),
		makeMove(G1, coord(5, 5), coord(6, 9), _, G2),
		makeMove(G2, coord(5, 8), coord(6, 8), _, O),
		gameWinner(O, n).

% yes
testBoard11SneakyKingNotCaptured :-
		newGame(tawlbwrdd, G),
		makeMove(G, coord(6, 9), coord(7, 9), _, G1),
		makeMove(G1, coord(6, 6), coord(6, 10), _, G2),
		makeMove(G2, coord(7, 9), coord(8, 9), _, O),
		gameWinner(O, n).

% yes
testBoard7KingNextToThroneCaptured3Sides :-
		newGame(brandubh, G),
		makeMove(G, coord(1, 4), coord(3, 3), _, G1),
		makeMove(G1, coord(4, 4), coord(4, 3), _, G2),
		makeMove(G2, coord(2, 4), coord(5, 3), _, O),
		gameWinner(O, b).

% yes
testBoard7Draw :-
		newGame(brandubh, G),
		makeMove(G, coord(4, 6), coord(1, 6), _, G1),
		makeMove(G1, coord(3, 4), coord(3, 7), _, G2),
		makeMove(G2, coord(2, 4), coord(2, 7), _, G3),
		makeMove(G3, coord(5, 4), coord(5, 7), _, G4),
		makeMove(G4, coord(6, 4), coord(6, 7), _, G5),
		makeMove(G5, coord(4, 3), coord(1, 3), _, G6),
		makeMove(G6, coord(4, 2), coord(1, 2), _, G7),
		makeMove(G7, coord(4, 5), coord(7, 5), _, G8),
		makeMove(G8, coord(1, 6), coord(7, 6), _, G9),
		makeMove(G9, coord(4, 4), coord(4, 6), _, G10),
		makeMove(G10, coord(4, 7), coord(3, 7), _, G11),
		makeMove(G11, coord(4, 6), coord(4, 7), _, G12),
		makeMove(G12, coord(7, 6), coord(5, 6), _, G13),
		makeMove(G13, coord(4, 7), coord(4, 6), _, G14),
		makeMove(G14, coord(6, 7), coord(5, 7), _, G15),
		makeMove(G15, coord(4, 6), coord(4, 7), _, G16),
		makeMove(G16, coord(5, 6), coord(4, 6), _, O),
		gameWinner(O, d).

% no
testIllegalMoveWrongTurnTeleport :- newGame(hnefatafl, G), makeLegitMove(G, coord(6, 6), coord(1, 1), _, O).

% no
testIllegalMoveFromEmptyCell :- newGame(hnefatafl, G), makeLegitMove(G, coord(1, 2), coord(11, 1), _, O).

% no
testIllegalMoveToSpecialCell :- newGame(hnefatafl, G), makeLegitMove(G, coord(1, 4), coord(1, 1), _, O).

% no
testIllegalMoveOnOccupiedCell :- newGame(hnefatafl, G), makeLegitMove(G, coord(1, 4), coord(1, 5), NC, O).

% no
testIllegalMoveNotOrthogonal :- newGame(hnefatafl, G), makeLegitMove(G, coord(1, 4), coord(2, 3), NC, O).

% yes
testLegalMove:- newGame(hnefatafl, G), makeLegitMove(G, coord(1, 4), coord(1, 3), NC, O).

% no
testIllegalMoveWrongTurnOrthogonal :- newGame(hnefatafl, G), makeLegitMove(G, coord(4, 6), coord(4, 5), NC, O).

% no
testIllegalMoveOnSameCoord :- newGame(hnefatafl, G), makeLegitMove(G, coord(1,4), coord(1,4), _, G1).

% yes
allTestsCaptures :- testHorizCapture, testVertCapture, testDoubleCapture, testTripleCapture.

% yes
allTestsScenarios :-
		testWhiteWin,
		testWhiteWin1,
		testBoard11KingCaptured3SidesAndThrone,
		testBoard11KingNotCaptured3Sides,
		testBoard11KingNotCapturedOnEdge,
		testBoard11KingOnThroneCapture,
		testBoard11KingFarFromThroneCapture,
		testBoard9KingOnThroneCapture4Sides,
		testBoard7KingOnThroneNoCapture3Sides,
		testBoard9KingFarFromThroneVertCapture,
		testBoard9KingFarFromThroneHorizCapture,
		testBoard9SneakyKingNotCaptured,
		testBoard11SneakyKingNotCaptured,
		testBoard7KingNextToThroneCaptured3Sides,
		testBoard7Draw.

% yes
allTestsLegalMoves :-
		not(testIllegalMoveWrongTurnTeleport),
		not(testIllegalMoveFromEmptyCell),
		not(testIllegalMoveToSpecialCell),
		not(testIllegalMoveOnOccupiedCell),
		not(testIllegalMoveNotOrthogonal),
		testLegalMove,
		not(testIllegalMoveWrongTurnOrthogonal),
		not(testIllegalMoveOnSameCoord).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%        One Test to rule them all, One Test to find them, One Test          %%
%%            to bring them all, and in the darkness bind them                %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


% yes
allTests :-
		allTestsLists,
		allTestsBoard,
		allTestsPossibleMoves,
		allTestsCaptures,
		allTestsScenarios,
		allTestsLegalMoves.