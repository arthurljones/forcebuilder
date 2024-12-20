package tech.ajones.forcebuilder.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Damage(
  @SerialName("dmgS")
  val short: String = "0",
  @SerialName("dmgM")
  val medium: String = "0",
  @SerialName("dmgL")
  val long: String = "0",
  @SerialName("dmgE")
  val extreme: String = "0",
)

@Serializable
data class UnitVariant(
  /**
   * The IS/general chassis name for this unit
   */
  @SerialName("chassis")
  val chassis: String = "",
  /**
   * The clan chassis name for this unit
   */
  @SerialName("clanChassis")
  val clanChassis: String = "",
  /**
   * The model/variant of the chassis
   */
  @SerialName("model")
  val variant: String = "",
  /**
   * The Alpha Strike points value for this variant
   */
  @SerialName("PV")
  val pointsValue: Int = 0,
  /**
   * Alpha Strike unit type
   */
  @SerialName("TP")
  val type: String = "Unknown",
  /**
   * Unit movement profile in Alpha Strike format
   */
  @SerialName("MV")
  val movement: String = "",
  /**
   * Unit overheat levels for Alpha Strike
   */
  @SerialName("OV")
  val overheat: Int = 0,
  /**
   * Unit size for Alpha Strike
   */
  @SerialName("SZ")
  val size: Int = 1,
  /**
   * Alpha Strike Target Movement Modifier
   */
  @SerialName("TMM")
  val tmm: Int = 0,
  /**
   * Unit's Alpha Strike structure points
   */
  @SerialName("Str")
  val structure: Int = 0,
  /**
   * Unit's Alpha Strike armor points
   */
  @SerialName("Arm")
  val armor: Int = 0,
  /**
   * The S/M/L(/E) Alpha Strike damage profile for this unit
   */
  @SerialName("dmg")
  val damage: Damage = Damage(),
  /**
   * Special abilities for this unit in Alpha Strike format
   */
  @SerialName("specials")
  val specials: String = "",

  /**
   * Whether this unit is clan tech or not
   */
  @SerialName("clan")
  val isClan: Boolean = false,
  /**
   * The Master Unit List id for this unit
   */
  @SerialName("mulId")
  val mulId: Int? = 0,
  /**
   * Whether this is a canon BattleTech unit
   */
  @SerialName("canon")
  val isCanon: Boolean = false,
  /**
   * Whether this is a support (as opposed to combat) unit
   */
  @SerialName("support")
  val isSupport: Boolean = false,

  /**
   * What combat role this unit performs
   */
  @SerialName("role")
  val role: String = "Undetermined",

  /**
   * What year this variant was first introduced/created
   */
  @SerialName("year")
  val yearIntroduced: Int = 0,

  /**
   * What year this variant became Advanced tech from a higher tech level
   */
  @SerialName("advTechYear")
  val advancedTechYear: Int = 0,

  /**
   * What year this variant became Standard tech from a higher tech level
   */
  @SerialName("stdTechYear")
  val standardTechYear: Int = 0,

  /**
   * Whether this variant is capable of firing at extreme range
   */
  @SerialName("usesE")
  val usesExtremeRange: Boolean = false,

  // Unused data for now
  //  "techBase", // Values: IS, Clan, MixedIS, MixedClan
  //  techLevel, // Static year-independent tech level.
  //    //Values: Introductory, Standard, Advanced, Experimental, Unofficial);

  // Unused data (probably forever)
  //  "usesArcs",
  //  "usesE",
  //  "usesOV",
  //  "usesTh",
  //  "Th",
  //  "frontArc",
  //  "leftArc",
  //  "rearArc",
  //  "rightArc",

) {
  override fun toString(): String = "$chassis $variant PV: $pointsValue"

  /**
   * The chassis of this variant according to its tech base
   */
  val preferredChassis: String = clanChassis.takeIf { isClan && it.isNotBlank() } ?: chassis

  val damageString: String
    get() = listOfNotNull(
      damage.short,
      damage.medium,
      damage.long,
      damage.extreme.takeIf { usesExtremeRange }
    ).joinToString("/")

  val armorStructureString: String
    get() = "$armor/$structure"

}