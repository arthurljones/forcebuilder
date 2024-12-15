package tech.ajones.forcebuilder.model

interface EraSpan {
  val start: Int
  val end: Int
}

enum class SubEra(override val start: Int, override val end: Int): EraSpan {
  AgeOfWar(start = 2005, end = 2570),
  StarLeague(start = 2571, end = 2780),
  EarlySuccessionWar(start = 2781,end = 2900),
  LateSuccessionWarLosTech(start = 2901,end = 3019),
  LateSuccessionWarRenaissance(start = 3020,end = 3049),
  ClanInvasion(start = 3050,end = 3061),
  CivilWar(start = 3062,end = 3067),
  Jihad(start = 3068,end = 3080),
  EarlyRepublic(start = 3081,end = 3100),
  LateRepublic(start = 3101,end = 3130),
  DarkAge(start = 3031,end = 3150),
  IlClan(start = 3151, end = 9999),
}

enum class Era(val subEras: List<SubEra>): EraSpan {
  StarLeague(subEras = listOf(
    SubEra.AgeOfWar,
    SubEra.StarLeague
  )),
  SuccessionWars(subEras = listOf(
    SubEra.EarlySuccessionWar,
    SubEra.LateSuccessionWarLosTech,
    SubEra.LateSuccessionWarRenaissance
  )),
  ClanInvasion(subEras = listOf(SubEra.ClanInvasion)),
  CivilWar(subEras = listOf(SubEra.CivilWar)),
  Jihad(subEras = listOf(SubEra.Jihad)),
  DarkAge(subEras = listOf(
    SubEra.EarlyRepublic,
    SubEra.LateRepublic,
    SubEra.DarkAge
  )),
  IlClan(subEras = listOf(SubEra.IlClan)),
  ;

  override val start: Int = subEras.minOf { it.start }
  override val end: Int = subEras.maxOf { it.end }
}
