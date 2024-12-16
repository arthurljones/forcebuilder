package tech.ajones.forcebuilder.model

/**
 * Unit special abilities.
 *
 * Extracted from a PDF found online that I _believe_ is from
 * [this blog post](https://nckestrel.wordpress.com/2021/09/01/alpha-strike-movement-cost-table-for-hex-maps/),
 * though the link there no longer works.
 */
@Suppress("unused")
enum class SpecialAbility(
  /**
   * The special code for this special ability
   */
  val special: String,
  /**
   * The full name of this special ability
   */
  val fullName: String,
  /**
   * The page in Alpha Strike: Commander's Edition (3rd edition, 2021) where this
   * special is described
   */
  val page: Int,
  /**
   * Simple summary of this special ability
   */
  val summary: String
) {
  ABA(
    special = "ABA",
    fullName = "Anti-Penetrative Ablation Armor",
    page = 83,
    summary = "Ignores tasers, armor penetrating/tandem charge"
  ),
  AECM(
    special = "AECM",
    fullName = "Angel ECM",
    page = 76,
    summary = "Advanced ECM"
  ),
  AFC(
    special = "AFC",
    fullName = "Advanced Fire Control",
    page = 76,
    summary = "IM and SV negate basic fire control negatives"
  ),
  AM(
    special = "AM",
    fullName = "Anti-‘Mech",
    page = 76,
    summary = "Infantry physical attack"
  ),
  AMP(
    special = "AMP",
    fullName = "Amphibious",
    page = 76,
    summary = "Unit can move as surface naval"
  ),
  AMS(
    special = "AMS",
    fullName = "Anti-Missile System",
    page = 76,
    summary = "Reduce damage from IF, LRM, SRM"
  ),
  ARM(
    special = "ARM",
    fullName = "Armored Component",
    page = 76,
    summary = "Ignore first critical hit chance"
  ),
  ARS(
    special = "ARS",
    fullName = "Armored Motive System",
    page = 77,
    summary = "Reduced threat from motive hits"
  ),
  ARTX(
    special = "ARTX-#",
    fullName = "Artillery",
    page = 82,
    summary = "Artillery attack of type X"
  ),
  AT(
    special = "AT#",
    fullName = "Aerospace Transport",
    page = 82,
    summary = "Can transport # aerospace units, D# is number of doors"
  ),
  ATAC(
    special = "ATAC#",
    fullName = "Advanced Tactical Analysis Computer",
    page = 82,
    summary = "Improved targeting (-1 TN) for # robotic units"
  ),
  BAR(
    special = "BAR",
    fullName = "Barrier Armor Rating",
    page = 77,
    summary = "Inferior armor with critical chance on all hits"
  ),
  BFC(
    special = "BFC",
    fullName = "Basic Fire Control",
    page = 77,
    summary = "Inferior fire control with attack penalty"
  ),
  BH(
    special = "BH",
    fullName = "Bloodhound Active Probe",
    page = 83,
    summary = "12”/26” detection range for hidden units/scan"
  ),
  BHJ(
    special = "BHJ",
    fullName = "BattleMech HarJel",
    page = 77,
    summary = "Negates hull breaches"
  ),
  BHJ2(
    special = "BHJ2",
    fullName = "BattleMech HarJel II",
    page = 83,
    summary = "Recover damaged armor, ignore hull breach"
  ),
  BHJ3(
    special = "BHJ3",
    fullName = "BattleMech HarJel III",
    page = 83,
    summary = "Recover more damaged armor, ignore hull breach"
  ),
  BIM(
    special = "BIM (#a)",
    fullName = "Bimodal Land-Air BattleMech",
    page = 83,
    summary = "Convert to aero and back to ‘Mech"
  ),
  BOMB(
    special = "BOMB",
    fullName = "Bomb",
    page = 77,
    summary = "Can carry bombs, carried bomb reduce thrust 1 each"
  ),
  BRA(
    special = "BRA",
    fullName = "Ballistic-Reinforced Armor",
    page = 83,
    summary = "Reduces AC,IATM,IF,LRM,SRM damage"
  ),
  BRID(
    special = "BRID",
    fullName = "Bridgelayer",
    page = 83,
    summary = "Deploy a temp bridge 2” long"
  ),
  BT(
    special = "BT",
    fullName = "Booby Trap",
    page = 83,
    summary = "Self-destruct"
  ),
  BTAS(
    special = "BTAS#",
    fullName = "Battle Armor Taser",
    page = 90,
    summary = "# attacks that can interfere/shutdown target"
  ),
  C3BSM(
    special = "C3BSM",
    fullName = "Boosted System C3 Master",
    page = 80,
    summary = "Can join C3BS network or C3 network as master"
  ),
  C3BSS(
    special = "C3BSS",
    fullName = "Boosted System C3 Slave",
    page = 80,
    summary = "Can join C3BS network or C3 network as slave"
  ),
  C3EM(
    special = "C3EM",
    fullName = "C3 Emergency Master",
    page = 82,
    summary = "Can replace C3M in network mid-game"
  ),
  C3I(
    special = "C3I",
    fullName = "C3 Improved",
    page = 82,
    summary = "Can join C3I network"
  ),
  C3M(
    special = "C3M",
    fullName = "C3 Master",
    page = 80,
    summary = "Can join C3 network as master"
  ),
  C3RS(
    special = "C3RS",
    fullName = "C3 Remote Sensor",
    page = 82,
    summary = "Deploy up to 4 remote sensors as C3 slaves"
  ),
  C3S(
    special = "C3S",
    fullName = "C3 Slave",
    page = 80,
    summary = "Can join C3 network as slave"
  ),
  CAP(
    special = "CAP",
    fullName = "Capital Weapons",
    page = 84,
    summary = "Weapons intended for really large targets"
  ),
  CAR(
    special = "CAR#",
    fullName = "Cargo",
    page = 77,
    summary = "Infantry that can be mounted in unit with IT#"
  ),
  CASE(
    special = "CASE",
    fullName = "Cellular Ammunition Storage Equipment",
    page = 77,
    summary = "Reduce Ammo Hit critical hit effect"
  ),
  CASEII(
    special = "CASEII",
    fullName = "Cellular Ammunition Storage Equipment II",
    page = 77,
    summary = "Ignore Ammo Hit critical hit effect"
  ),
  CASEP(
    special = "CASEP",
    fullName = "Prototype CASE",
    page = 88,
    summary = "Can prevent Ammo Hit critical hit"
  ),
  CK(
    special = "CK#",
    fullName = "Cargo Transport, Kilotons",
    page = 84,
    summary = "Can transport cargo, D# is number of doors"
  ),
  CNARC(
    special = "CNARC#",
    fullName = "Compact Narc Missile Beacon",
    page = 87,
    summary = "# attack(s) to attach beacon improve IF, LRM, SRM, affected by ECM"
  ),
  CR(
    special = "CR",
    fullName = "Critical-Resistant",
    page = 84,
    summary = "-2 to critical hits roll"
  ),
  CRW(
    special = "CRW#",
    fullName = "Crew",
    page = 84,
    summary = "Use crew as infantry units equal to #"
  ),
  D(
    special = "D#",
    fullName = "Door",
    page = 84,
    summary = "# of units per turn that can embark/disembark"
  ),
  DCC(
    special = "DCC#",
    fullName = "Drone Carrier Control System",
    page = 85,
    summary = "Control # DRO units"
  ),
  DJ(
    special = "DJ",
    fullName = "RISC Viral Jammer - Decoy",
    page = 89,
    summary = "Can render AECM,ECM,LECM,STL, WAT inoperative"
  ),
  DNI(
    special = "DNI",
    fullName = "Direct Neural Control System",
    page = 84,
    summary = "If crew has DNI implant, -1 to Skill"
  ),
  DRO(
    special = "DRO",
    fullName = "Drone",
    page = 84,
    summary = "Robotic +1 Skill, affected by ECM, need control unit"
  ),
  DUN(
    special = "DUN",
    fullName = "Dune Buggy",
    page = 85,
    summary = "Better Sand movement"
  ),
  ECM(
    special = "ECM",
    fullName = "Electronic CounterMeasures",
    page = 77,
    summary = "12” radius effect against xPRB, DRO, xNARC, C3x"
  ),
  ECS(
    special = "ECS",
    fullName = "RISC Emergency Coolant System",
    page = 89,
    summary = "Triggers at heat scale 4, can reduce heat by 2, high fail rate"
  ),
  EE(
    special = "EE",
    fullName = "Elementary Engine",
    page = 77,
    summary = "No underwater w/o SEAL, no vacuum, change Engine Hit critical hit"
  ),
  ENE(
    special = "ENE",
    fullName = "Energy",
    page = 77,
    summary = "Ignore Ammo Hit critical hit"
  ),
  ENG(
    special = "ENG",
    fullName = "Engineering",
    page = 85,
    summary = "Clear path through woods or rubble"
  ),
  ES(
    special = "ES",
    fullName = "Ejection Seat",
    page = 85,
    summary = "See Ejection rules."
  ),
  FC(
    special = "FC",
    fullName = "Fuel Cell Engine",
    page = 77,
    summary = "No underwater or vacuum w/o SEAL, change Engine Hit critical hit"
  ),
  FD(
    special = "FD",
    fullName = "Flight Deck",
    page = 85,
    summary = "Landing area for air units"
  ),
  FF(
    special = "FF",
    fullName = "Firefighter",
    page = 85,
    summary = "Can put out fires"
  ),
  FLK(
    special = "FLK#/#/#/#",
    fullName = "Flak",
    page = 78,
    summary = "Improved ability to attack airborne units"
  ),
  FR(
    special = "FR",
    fullName = "Fire Resistant",
    page = 78,
    summary = "Ignore Heat (HT) effects"
  ),
  GLD(
    special = "GLD",
    fullName = "Glider ProtoMech",
    page = 85,
    summary = "WiGE for protomechs"
  ),
  HELI(
    special = "HELI",
    fullName = "Helipad",
    page = 85,
    summary = "Landing area for VTOL"
  ),
  HJ(
    special = "HJ",
    fullName = "RISC Viral Jammer - Homing",
    page = 89,
    summary = "Can render TAG, C3, NOVA inoperative. hinders IATM, LRM,CNARC, SNARC, SRM attacks"
  ),
  HPG(
    special = "HPG",
    fullName = "HyperPulse Generator",
    page = 85,
    summary = "In battle, can create HPG overload pulse effect"
  ),
  HT(
    special = "HT#/#/#",
    fullName = "Heat",
    page = 78,
    summary = "Attack can increase Heat scale of target"
  ),
  HTC(
    special = "HTC",
    fullName = "Trailer Hitch",
    page = 90,
    summary = "Can tow other wheeled/tracked units and trailers"
  ),
  IATM(
    special = "IATM#/#/#/#",
    fullName = "Improved ATM",
    page = 86,
    summary = "Can use Improved ATM alternate munitions"
  ),
  IF(
    special = "IF#",
    fullName = "Indirect Fire",
    page = 78,
    summary = "Can make indirect fire attack"
  ),
  IRA(
    special = "IRA",
    fullName = "Impact-Resistant Armor",
    page = 86,
    summary = "Reduce physical attack damage, +1 to critical hit effects"
  ),
  IT(
    special = "IT#",
    fullName = "Infantry Transport",
    page = 78,
    summary = "Can transport infantry"
  ),
  I(
    special = "I-TSM",
    fullName = "Industrial Triple-Strength Myomers",
    page = 78,
    summary = "Increased physical attack damage but with attack penalty"
  ),
  JAM(
    special = "JAM",
    fullName = "SDS Jammer",
    page = 89,
    summary = "Reduces ATAC,NC3 bonus"
  ),
  JMPS(
    special = "JMPS#",
    fullName = "Jump Jets, Strong",
    page = 78,
    summary = "Higher jump TMM by #"
  ),
  JMPW(
    special = "JMPW#",
    fullName = "Jump Jets, Weak",
    page = 78,
    summary = "Lower jump TMM by #"
  ),
  LAM(
    special = "LAM (#g/#a)",
    fullName = "Land-Air BattleMech",
    page = 86,
    summary = "Convert to aero, AirMech or ‘Mech"
  ),
  LECM(
    special = "LECM",
    fullName = "Light ECM",
    page = 78,
    summary = "2” radium ECM"
  ),
  LG(
    special = "LG",
    fullName = "Large",
    page = 86,
    summary = "2” area, blocks LOS, easier to hit"
  ),
  LMAS(
    special = "LMAS",
    fullName = "Light Mimetic Armor System",
    page = 78,
    summary = "Stealth +2 when stationary"
  ),
  LPRB(
    special = "LPRB",
    fullName = "Light Active Probe",
    page = 86,
    summary = "6”/12” detect hidden units/scan"
  ),
  LRM(
    special = "LRM#/#/#/#",
    fullName = "Long-Range Missiles",
    page = 86,
    summary = "Can use LRM alternate munitions"
  ),
  LTAG(
    special = "LTAG",
    fullName = "Light TAG",
    page = 86,
    summary = "Short range, improves artillery homing"
  ),
  MAG(
    special = "MAG",
    fullName = "Maglev",
    page = 86,
    summary = "Magnetic levitation rail movement"
  ),
  MAS(
    special = "MAS",
    fullName = "Mimetic Armor System",
    page = 78,
    summary = "Stealth +3 when stationary"
  ),
  MASH(
    special = "MASH#",
    fullName = "Mobile Army Surgical Hospital",
    page = 87,
    summary = "Can act as half # IT, repair infantry?"
  ),
  MCS(
    special = "MCS",
    fullName = "Magnetic Clamp System",
    page = 87,
    summary = "Protomech mount BattleMech as with XMEC BA"
  ),
  MDS(
    special = "MDS#",
    fullName = "Mine Dispenser",
    page = 87,
    summary = "Can create minefields"
  ),
  MEC(
    special = "MEC",
    fullName = "Mechanized",
    page = 78,
    summary = "BA that can mount OMNI units"
  ),
  MEL(
    special = "MEL",
    fullName = "Melee",
    page = 78,
    summary = "Can make melee physical attack +1 damage"
  ),
  MFB(
    special = "MFB",
    fullName = "Mobile Field Base",
    page = 87,
    summary = "No effect during battle, repair units?"
  ),
  MHQ(
    special = "MHQ#",
    fullName = "Mobile Headquarters",
    page = 87,
    summary = "Provide BattleField Intelligence"
  ),
  MSL(
    special = "MSL#/#/#/#",
    fullName = "Missile",
    page = 87,
    summary = "CAP or SCAP missiles, artillery attacks against ground units"
  ),
  MSW(
    special = "MSW",
    fullName = "Minesweeper",
    page = 87,
    summary = "Clear minefields"
  ),
  MT(
    special = "MT#",
    fullName = "‘Mech Transport",
    page = 87,
    summary = "Can transport # ‘Mechs"
  ),
  MTAS(
    special = "MTAS#",
    fullName = "‘Mech Taser",
    page = 90,
    summary = "# attacks that can interfere/shutdown target"
  ),
  MTN(
    special = "MTN",
    fullName = "Mountain Troops",
    page = 87,
    summary = "Infantry that can cross greater level changes"
  ),
  NC3(
    special = "NC3",
    fullName = "Naval C3",
    page = 87,
    summary = "Can join NC3 network"
  ),
  NOVA(
    special = "NOVA",
    fullName = "Nova Composite EW System",
    page = 87,
    summary = "Duplicates ECM, PRB, special C3I"
  ),
  OMNI(
    special = "OMNI",
    fullName = "Omni",
    page = 78,
    summary = "Can transport MEC Battle Armor"
  ),
  ORO(
    special = "ORO",
    fullName = "Off-Road",
    page = 78,
    summary = "Doesn’t pay the extra SV move for non-paved"
  ),
  OVL(
    special = "OVL",
    fullName = "Overheat Long",
    page = 78,
    summary = "Can use overheat (OV) at long range"
  ),
  PAR(
    special = "PAR",
    fullName = "Paratroops",
    page = 87,
    summary = "Dismount from airborne as jump infantry"
  ),
  PNT(
    special = "PNT#",
    fullName = "Point Defense",
    page = 87,
    summary = "Reduces IF, SRM, LRM, ARTAIV, MSL by #"
  ),
  PRB(
    special = "PRB",
    fullName = "Active Probe",
    page = 82,
    summary = "10”/18” detection range for hidden units/scan"
  ),
  PT(
    special = "PT#",
    fullName = "ProtoMech Transport",
    page = 88,
    summary = "Can transport # ProtoMechs"
  ),
  QV(
    special = "QV",
    fullName = "Quadvee",
    page = 88,
    summary = "Uses quadvee rules"
  ),
  RAIL(
    special = "RAIL",
    fullName = "Rail",
    page = 88,
    summary = "Moves along rails"
  ),
  RAMS(
    special = "RAMS",
    fullName = "RISC Advanced Point Defense System",
    page = 89,
    summary = "AMS affect that can also defend adjacent units"
  ),
  RBT(
    special = "RBT",
    fullName = "Robotic Drone",
    page = 89,
    summary = "Robotic unit that doesn’t need control."
  ),
  RCA(
    special = "RCA",
    fullName = "Reactive Armor",
    page = 88,
    summary = "Reduce area effect, IF, SRM, LRM damage"
  ),
  RCN(
    special = "RCN",
    fullName = "Recon",
    page = 88,
    summary = "Provides BattleField Intelligence"
  ),
  REAR(
    special = "REAR#/#/#/#",
    fullName = "Rear-Firing Weapons",
    page = 78,
    summary = "Can make an attack from the rear firing arc"
  ),
  REL(
    special = "REL",
    fullName = "Re-Engineered Lasers",
    page = 88,
    summary = "Ignore RFA, reduce CR"
  ),
  RFA(
    special = "RFA",
    fullName = "Reflective Armor",
    page = 88,
    summary = "Reduce ENE, HT damage, general. increase physical, AE"
  ),
  RHS(
    special = "RHS",
    fullName = "Radical Heat Sink System",
    page = 88,
    summary = "Reduce heat scale by 1, may fail"
  ),
  RSD(
    special = "RSD#",
    fullName = "Remote Sensor Dispenser",
    page = 88,
    summary = "Deploy sensors to detect/scan"
  ),
  SAW(
    special = "SAW",
    fullName = "Saw",
    page = 89,
    summary = "Can clear woods"
  ),
  SCAP(
    special = "SCAP",
    fullName = "Sub-Capital",
    page = 90,
    summary = "Weapons intended for really large targets"
  ),
  SDCS(
    special = "SDCS",
    fullName = "SDS Drone Control System",
    page = 89,
    summary = "Superior robotic drone"
  ),
  SD_SC(
    special = "SDS-C #/#/#/#",
    fullName = "Space Defense System, Capital",
    page = 90,
    summary = "Weapons intended for really large targets"
  ),
  SDS_CM(
    special = "SDS-CM #/#/#/#",
    fullName = "Space Defense System, Capital Missile",
    page = 90,
    summary = "Weapons intended for really large targets"
  ),
  SDS_SC(
    special = "SDS-SC",
    fullName = "Space Defense System, Subcapital",
    page = 90,
    summary = "Weapons intended for really large targets"
  ),
  SEAL(
    special = "SEAL",
    fullName = "Environmental Sealing",
    page = 85,
    summary = "Survive underwater, vacuum"
  ),
  SHLD(
    special = "SHLD",
    fullName = "BattleMech Shield",
    page = 77,
    summary = "Reduce damage from most attacks, but attack penalty"
  ),
  SLG(
    special = "SLG",
    fullName = "Super Large",
    page = 90,
    summary = "6” area, block LOS, easier to hit"
  ),
  SNARC(
    special = "SNARC#",
    fullName = "Standard Narc Missile Beacon",
    page = 87,
    summary = "# attack(s) to attach beacon improve IF, LRM, SRM, affected by ECM"
  ),
  SOA(
    special = "SOA",
    fullName = "Space Operations Adaptation",
    page = 90,
    summary = "Can operate in vacuum, no spaceflight"
  ),
  SRCH(
    special = "SRCH",
    fullName = "Searchlight",
    page = 90,
    summary = "Ignore darkness modifiers"
  ),
  SRM(
    special = "SRM#/#",
    fullName = "Short Range Missiles",
    page = 90,
    summary = "Can use SRM alternate munitions"
  ),
  ST(
    special = "ST#",
    fullName = "Small Craft Transport",
    page = 90,
    summary = "Can transport # small craft"
  ),
  STL(
    special = "STL",
    fullName = "Stealth",
    page = 79,
    summary = "Harder to hit (range dependent)"
  ),
  SUBS(
    special = "SUBS#",
    fullName = "Submersible Movement, Strong",
    page = 79,
    summary = "Higher submersible TMM by #"
  ),
  SUBW(
    special = "SUBW#",
    fullName = "Submersible Movement, Weak",
    page = 79,
    summary = "Lower submersible TMM By #"
  ),
  TAG(
    special = "TAG",
    fullName = "Target Acquisition Gear",
    page = 90,
    summary = "Medium range, improves artillery homing"
  ),
  TOR(
    special = "TOR#/#/#",
    fullName = "Torpedo",
    page = 79,
    summary = "Underwater/surface water attack"
  ),
  TRN(
    special = "TRN",
    fullName = "Trenchworks/Fieldworks Engineers",
    page = 91,
    summary = "Can create fortified area for infantry"
  ),
  TSEMP(
    special = "TSEMP#",
    fullName = "Tight-Stream ElectroMagnetic Pulse Weapons",
    page = 90,
    summary = "# attacks that can interfere/shutdown target"
  ),
  TSI(
    special = "TSI",
    fullName = "Triple-Strength Implants",
    page = 91,
    summary = "Cybernetic enhanced infantry"
  ),
  TSM(
    special = "TSM",
    fullName = "Triple-Strength Myomer",
    page = 79,
    summary = "+2” MV and +1 physical damage when heat scale 1+"
  ),
  TSMX(
    special = "TSMX",
    fullName = "Prototype Triple-Strength Myomer",
    page = 88,
    summary = "+1 damage to all physical attacks, vulnerable to Anti-TSM"
  ),
  TUR(
    special = "TUR(#/#/#/#)",
    fullName = "Turret",
    page = 79,
    summary = "360-degree firing arc, using TUR damage/abilities only"
  ),
  UCS(
    special = "UCS",
    fullName = "Magnetic Clamp System",
    page = 87,
    summary = "Protomech mount BattleMech as with XMEC BA"
  ),
  UMU(
    special = "UMU",
    fullName = "Underwater Maneuvering Units",
    page = 80,
    summary = "Use submersible movement rules when submerged"
  ),
  VLG(
    special = "VLG",
    fullName = "Very Large",
    page = 91,
    summary = "4” area, block LOS, easier to hit"
  ),
  VR(
    special = "VR",
    fullName = "Virtual Reality Piloting Pod",
    page = 91,
    summary = "Improving piloting, affected by ECM"
  ),
  VRT(
    special = "VRT",
    fullName = "Variable-Range Targeting",
    page = 91,
    summary = "Switch to short/long/standard targeting"
  ),
  VSTOL(
    special = "VSTOL",
    fullName = "Very-Short Take Off and Landing",
    page = 91,
    summary = "Shorter takeoff/landing distance"
  ),
  VTH(
    special = "VTH#",
    fullName = "Heavy Vehicle Transport",
    page = 91,
    summary = "Can transport # size 1-4 vehicles"
  ),
  VTM(
    special = "VTM#",
    fullName = "Medium Vehicle Transport",
    page = 91,
    summary = "Can transport # size 1 or 2 vehicles"
  ),
  VTS(
    special = "VTS#",
    fullName = "Super-Heavy Vehicle Transport",
    page = 91,
    summary = "Can transport # vehicles including LG"
  ),
  WAT(
    special = "WAT",
    fullName = "Watchdog",
    page = 80,
    summary = "Combined ECM and LPRB"
  ),
  XMEC(
    special = "XMEC",
    fullName = "Extended Mechanized",
    page = 78,
    summary = "BA that can mount any ground unit, not just OMNI"
  )
}
